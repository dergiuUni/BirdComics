// validation.js
	var validName = false;
	var validDescription = false;
	var validPrice = false;
	var validQuantity = false;
	var validImageType = false;
	var validGenres = false;
	



	function checkName(input) {
		if (input.value.length < 1) {
			document.getElementById('nameText').innerHTML = "Devi inserire almeno 1 carattere";
			validName = false;
			return false;
		} else if (input.value.length > 250) {
			document.getElementById('nameText').innerHTML = "Hai inserito troppi caratteri";
			validName = false;
			return false;
		} else {
			document.getElementById('nameText').innerHTML = "";
			validName = true;
			return true;
		}
	}

	function checkDescription(input) {
		var des = input.value.trim();
		if (des.length < 1) {
			document.getElementById('descriptionText').innerHTML = "Devi inserire almeno 1 carattere";
			validDescription = false;
			return false;
		} else if (des.length > 700) {
			document.getElementById('descriptionText').innerHTML = "Hai inserito troppi caratteri";
			validDescription = false;
			return false;
		} else {
			document.getElementById('descriptionText').innerHTML = "";
			validDescription = true;
			return true;
		}
	}

	function checkPrice(input) {
		if (input.value <= 0) {
			document.getElementById('priceText').innerHTML = "Il prezzo non puo essere 0 o meno";
			validPrice = false;
			return false;
		} else if (input.value > 999.99) {
			document.getElementById('priceText').innerHTML = "Il prezzo non puo superare 9999.99";
			validPrice = false;
			return false;
		} else if (!/^[\d.]+$/.test(input.value)) {
			document.getElementById('priceText').innerHTML = "Il prezzo non deve contenere lettere";
			validPrice = false;
			return false;
		} else {
			document.getElementById('priceText').innerHTML = "";
			validPrice = true;
			return true;
		}
	}

	function checkImageType(input) {
		var file = input.files[0];
		var fileType = file.type.toLowerCase();

		if (fileType !== 'image/png' && fileType !== 'image/gif'
				&& fileType !== 'image/jpeg' && fileType !== 'image/jpg') {
			document.getElementById('imageText').innerHTML = "Scegli un file PNG, GIF, JPEG o JPG valido";
			validImageType = false;
			return false;
		} else {
			document.getElementById('imageText').innerHTML = "";
			validImageType = true;
			return true;
		}
	}
	
	function checkGenres() {
    var checkboxes = document.getElementsByName("genres");
    var isChecked = false;

    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            isChecked = true;
            break;
        }
    }

    if (!isChecked) {
        document.getElementById('genreText').innerHTML = "Seleziona almeno un genere!";
        validGenres = false;
        return false;
    } else {
        document.getElementById('genreText').innerHTML = "";
        validGenres = true;
        return true;
    }
}

	function validateForm() {
		
	    if (!validName) {
	        alert("Controlla il campo nome!!");
	        return false; 
	    }

	    if (!validDescription) {
	        alert("Controlla il campo descrizione!!");
	        return false;
	    }
	
	    if (!validPrice) {
	        alert("Controlla il campo prezzo!!");
	        return false;
	    }
 		if (!validGenres) {
			 alert("Seleziona almeno un genere!");
        return false;
    }
	    if(!validImageType) {
	    	alert("Errore, scegli un altra foto!");
	        return false;
	    	
	    }

	    return true;
	}

