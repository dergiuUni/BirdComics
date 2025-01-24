  $(document).ready(function() {
            // Function to load all products via AJAX
            function loadAllProducts() {
                $.ajax({
                    url: 'ProductListServlet',
                    type: 'GET',
                    dataType: 'json', // Expecting JSON response
                    success: function(products) {
                        var productListHtml = '';

                        // Iterate through products and generate HTML
                        $.each(products, function(index,product) {
                            productListHtml += '<div class="col-sm-4" style="height: 350px;">';
                            productListHtml += '<div class="thumbnail">';
                            productListHtml += '<a href="./SingleProductServlet?pid=' + product.prodId + '">';
                            productListHtml += '<img src="./ShowImage?pid=' + product.prodId + '" alt="Product" style="height: 150px; max-width: 180px">';
                            productListHtml += '</a>';
                            productListHtml += '<p class="productname">' + product.prodName + '</p>';
                            productListHtml += '<p class="productinfo">' + product.prodInfo.substring(0, 100) + '...</p>';
                            productListHtml += '<p class="price">Euro ' + product.prodPrice + '</p>';
                            productListHtml += '<form method="post" action="AddToCart">';
                            productListHtml += '<input type="hidden" name="pid" value="' + product.prodId + '">';
                            productListHtml += '<button type="submit" class="btn btn-success">Add to Cart</button>';
                            productListHtml += '<input type="number" name="pqty" value="1" min="1" max="' + product.prodQuantity + '">';
                            productListHtml += '</form>';
                            productListHtml += '<br />';
                            productListHtml += '</div>';
                            productListHtml += '</div>';
                        });
                        

                        // Update the product list container with generated HTML
                        $('#product-list').html(productListHtml);
                    },
                    error: function(xhr, status, error) {
                        console.error('Error:', status, error);
                    }
                });
            }

            // Load all products when the page is ready
            loadAllProducts();

            // Function to load products by type via AJAX
            function loadProductsByType(type) {
                $.ajax({
                    url: 'ProductListServlet',
                    type: 'GET',
                    data: { type: type },
                    dataType: 'json', // Expecting JSON response
                    success: function(products) {
                        var productListHtml = '';

                        // Iterate through products and generate HTML
                        $.each(products, function(index,product) {
                            productListHtml += '<div class="col-sm-4" style="height: 350px;">';
                            productListHtml += '<div class="thumbnail">';
                            productListHtml += '<a href="./SingleProductServlet?pid=' + product.prodId + '">';
                            productListHtml += '<img src="./ShowImage?pid=' + product.prodId + '" alt="Product" style="height: 150px; max-width: 180px">';
                            productListHtml += '</a>';
                            productListHtml += '<p class="productname">' + product.prodName + '</p>';
                            productListHtml += '<p class="productinfo">' + product.prodInfo.substring(0, 100) + '...</p>';
                            productListHtml += '<p class="price">Euro ' + product.prodPrice + '</p>';
                            productListHtml += '<form method="post" action="AddToCart">';
                            productListHtml += '<input type="hidden" name="pid" value="' + product.prodId + '">';
                            productListHtml += '<button type="submit" class="btn btn-success">Add to Cart</button>';
                            productListHtml += '<input type="number" name="pqty" value="1" min="1" max="' + product.prodQuantity + '">';
                            productListHtml += '</form>';
                            productListHtml += '<br />';
                            productListHtml += '</div>';
                            productListHtml += '</div>';
                        });

                        // Update the product list container with generated HTML
                        $('#product-list').html(productListHtml);
                    },
                    error: function(xhr, status, error) {
                        console.error('Error:', status, error);
                    }
                });
            }
            
        
            // Event listener for genre links
            $('.genre-link').click(function(event) {
                event.preventDefault();
                var genreType = $(this).data('type');
                loadProductsByType(genreType);
            });
            
            $('a[href="index.jsp"]').on('click', function(event) {
                event.preventDefault(); // Prevent default action of the link
                loadAllProducts(); // Load all products when 'Home' is clicked
            });
            
           
                // Funzione per la ricerca dei prodotti tramite AJAX
                function searchProducts(keyword) {
                    $.ajax({
                        url: 'ProductListServlet',
                        type: 'GET',
                        data: { search: keyword },
                        dataType: 'json', // Si aspetta una risposta JSON
                        success: function(products) {
                            var productListHtml = '';

                            // Itera sui prodotti e genera l'HTML corrispondente
                            $.each(products, function(index, product) {
                                productListHtml += '<div class="col-sm-4" style="height: 350px;">';
                                productListHtml += '<div class="thumbnail">';
                                productListHtml += '<a href="./SingleProductServlet?pid=' + product.prodId + '">';
                                productListHtml += '<img src="./ShowImage?pid=' + product.prodId + '" alt="Product" style="height: 150px; max-width: 180px">';
                                productListHtml += '</a>';
                                productListHtml += '<p class="productname">' + product.prodName + '</p>';
                                productListHtml += '<p class="productinfo">' + product.prodInfo.substring(0, 100) + '...</p>';
                                productListHtml += '<p class="price">Euro ' + product.prodPrice + '</p>';
                                productListHtml += '<form method="post" action="AddToCart">';
                                productListHtml += '<input type="hidden" name="pid" value="' + product.prodId + '">';
                                productListHtml += '<button type="submit" class="btn btn-success">Add to Cart</button>';
                                productListHtml += '<input type="number" name="pqty" value="1" min="1" max="' + product.prodQuantity + '">';
                                productListHtml += '</form>';
                                productListHtml += '<br />';
                                productListHtml += '</div>';
                                productListHtml += '</div>';
                            });

                            // Aggiorna il contenuto dell'elemento #product-list con l'HTML generato
                            $('#product-list').html(productListHtml);
                        },
                        error: function(xhr, status, error) {
                            console.error('Errore nella ricerca:', status, error);
                        }
                    });
                }
                
                $('form.form-inline').submit(function(event) {
                    event.preventDefault(); // Evita il comportamento predefinito di submit

                    var keyword = $('.search-data').val(); // Ottieni il valore dalla casella di ricerca
                    searchProducts(keyword); // Esegui la ricerca dei prodotti
                });
            
        });