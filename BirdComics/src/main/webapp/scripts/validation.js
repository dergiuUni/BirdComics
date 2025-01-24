// validation.js
	var validName = false;
	var validDescription = false;
	var validPrice = false;
	var validQuantity = false;
	var validImageType = false;



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

	function checkQuantity(input) {
		if (input.value < 0) {
			document.getElementById('quantityText').innerHTML = "La quantita non può essere negativa";
			validQuantity = false;
			return false;
		} else if (input.value > 999) {
			document.getElementById('quantityText').innerHTML = "Hai inserito un numero troppo grande";
			validQuantity = false;
			return false;
		} else if (!/^\d+$/.test(input.value)) {
			document.getElementById('quantityText').innerHTML = "La quantità non deve contenere lettere";
			validQuantity = false;
			return false;
		} else {
			document.getElementById('quantityText').innerHTML = "";
			validQuantity = true;
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

	    if (!validQuantity) {
	        alert("Controlla il campo quantità!!");
	        return false;
	    }
	    
	    if(!validImageType) {
	    	alert("Errore, scegli un altra foto!");
	        return false;
	    	
	    }

	

	
	    return true;
	}

