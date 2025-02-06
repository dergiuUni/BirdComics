package com.birdcomics.Utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ShowImage")
public class ShowImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ShowImage() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ottieni il nome dell'immagine dai parametri della richiesta
        String imageName = request.getParameter("image");
        if (imageName == null || imageName.isEmpty()) {
            imageName = "default.jpg"; // Usa un'immagine di fallback se non viene passato alcun parametro
        }

        // Percorso dell'immagine relativo alla directory 'uploads' nel server
        String uploadPath = request.getServletContext().getRealPath("uploads/");

        // Crea il file con il percorso completo
        File imageFile = new File(uploadPath + File.separator + imageName);

        // Controlla se il file esiste
        if (!imageFile.exists() || imageFile.isDirectory()) {
            // Se il file non esiste, rispondi con un'immagine di fallback
            imageFile = new File(uploadPath + File.separator + "default.jpg");
            if (!imageFile.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Default image not found.");
                return;
            }
        }

        // Determina l'estensione del file per impostare il tipo MIME
        String fileExtension = getFileExtension(imageFile);
        if (fileExtension == null || (!fileExtension.equals("jpg") && !fileExtension.equals("png"))) {
            // Se l'estensione non è supportata, invia un errore
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported image format.");
            return;
        }

        // Leggi l'immagine
        BufferedImage originalImage = ImageIO.read(imageFile);
        response.setContentType("image/" + fileExtension); // Imposta il tipo MIME dell'immagine

        // Crea il flusso di output per inviare l'immagine al client
        try (ServletOutputStream sos = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            ImageIO.write(originalImage, fileExtension, baos); // Scrive l'immagine nel flusso
            sos.write(baos.toByteArray()); // Invia l'immagine al client
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // Metodo per ottenere l'estensione del file
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex > 0 && lastIndex < name.length() - 1) {
            return name.substring(lastIndex + 1).toLowerCase(); // Restituisce l'estensione in minuscolo
        }
        return null;
    }
}
