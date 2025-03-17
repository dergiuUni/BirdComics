package com.birdcomics.integration.GestioneCatalago;

import com.birdcomics.Bean.GenereBean;
import com.birdcomics.Dao.GenereDAO;
import com.birdcomics.GestioneCatalogo.Controller.AddProductSrv;
import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

class AddProductSrvTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CatalogoService catalogoService;

    @Mock
    private GenereDAO genereDAO;

    @Mock
    private Part filePart;

    @InjectMocks
    private AddProductSrv addProductSrv;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addProductSrv = new AddProductSrv();
        addProductSrv.catalogoService = catalogoService; // Inietta manualmente il mock del servizio
    }

    @Test
    void testAggiunaFumettoCatalogoSuccess() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("name")).thenReturn("Test Product");
        when(request.getParameterValues("genres")).thenReturn(new String[]{"1", "2"});
        when(request.getParameter("info")).thenReturn("Test Description");
        when(request.getParameter("price")).thenReturn("19.99");

        // Configura il mock del filePart (immagine)
        when(request.getPart("image")).thenReturn(filePart);
        when(filePart.getSize()).thenReturn(1024L); // Simula un file valido
        when(filePart.getSubmittedFileName()).thenReturn("test.jpg");
        doNothing().when(filePart).write(anyString()); // Simula il salvataggio del file

        // Configura il mock del ServletContext
        ServletContext servletContext = mock(ServletContext.class);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath("uploads/")).thenReturn("/path/to/uploads/");

        // Configura il mock del servizio per restituire uno stato di successo
        when(catalogoService.addFumetto(anyString(), anyString(), anyFloat(), anyString(), any(String[].class)))
                .thenReturn("Product added successfully");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("addProduct.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doPost
        addProductSrv.doPost(request, response);

        // Verifica che il metodo del servizio sia stato chiamato
        verify(catalogoService).addFumetto(anyString(), anyString(), anyFloat(), anyString(), any(String[].class));

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio corretto
        verify(request).setAttribute("message", "Product added successfully");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testAggiuntaFumettoCatalogo_InvalidFileType() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("name")).thenReturn("Test Product");
        when(request.getParameterValues("genres")).thenReturn(new String[]{"1", "2"});
        when(request.getParameter("info")).thenReturn("Test Description");
        when(request.getParameter("price")).thenReturn("19.99");

        // Configura il mock del filePart (immagine non valida)
        when(request.getPart("image")).thenReturn(filePart);
        when(filePart.getSize()).thenReturn(1024L); // Simula un file valido
        when(filePart.getSubmittedFileName()).thenReturn("test.txt"); // Estensione non valida

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("addProduct.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doPost
        addProductSrv.doPost(request, response);

        // Verifica che il metodo del servizio non sia stato chiamato
        verify(catalogoService, never()).addFumetto(anyString(), anyString(), anyFloat(), anyString(), any(String[].class));

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore
        verify(request).setAttribute("message", "Invalid file type! Only JPG, JPEG, and PNG are allowed.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testAggiuntaFumettoCatalogo_SQLException() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("name")).thenReturn("Test Product");
        when(request.getParameterValues("genres")).thenReturn(new String[]{"1", "2"});
        when(request.getParameter("info")).thenReturn("Test Description");
        when(request.getParameter("price")).thenReturn("19.99");

        // Configura il mock del filePart (immagine)
        when(request.getPart("image")).thenReturn(filePart);
        when(filePart.getSize()).thenReturn(1024L); // Simula un file valido
        when(filePart.getSubmittedFileName()).thenReturn("test.jpg");
        doNothing().when(filePart).write(anyString()); // Simula il salvataggio del file

        // Configura il mock del ServletContext
        ServletContext servletContext = mock(ServletContext.class);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath("uploads/")).thenReturn("/path/to/uploads/");

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(catalogoService.addFumetto(anyString(), anyString(), anyFloat(), anyString(), any(String[].class)))
                .thenThrow(new SQLException("Database error"));

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("addProduct.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doPost
        addProductSrv.doPost(request, response);

        // Verifica che il metodo del servizio sia stato chiamato
        verify(catalogoService).addFumetto(anyString(), anyString(), anyFloat(), anyString(), any(String[].class));

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore
        verify(request).setAttribute("message", "Database error occurred while adding the product.");
        verify(requestDispatcher).forward(request, response);
    }
}