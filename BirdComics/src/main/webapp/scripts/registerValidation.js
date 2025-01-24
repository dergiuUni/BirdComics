// validation.js
	var validName = false;
	var validEmail = false;
	var validAddress = false;
	var validMobile = false;
	var validPincode = false;

	function checkName(input) {
		if (input.value.length < 1) {
			document.getElementById('nameText').innerHTML = "Devi inserire almeno 1 carattere";
			validName = false;
			return false;
		} else if (input.value.length > 30) {
			document.getElementById('nameText').innerHTML = "Hai inserito troppi caratteri";
			validName = false;
			return false;
		} else {
			document.getElementById('nameText').innerHTML = "";
			validName = true;
			return true;
		}
	}

	function checkEmail(input) {
		var des = input.value.trim();
		if (des.length < 1) {
			document.getElementById('emailText').innerHTML = "Devi inserire almeno 1 carattere";
			validEmail = false;
			return false;
		} else if (des.length > 50) {
			document.getElementById('emailText').innerHTML = "Hai inserito troppi caratteri";
			validEmail = false;
			return false;
		} else {
			document.getElementById('emailText').innerHTML = "";
			validEmail = true;
			return true;
		}
	}

		function checkAddress(input) {
		var des = input.value.trim();
		if (des.length < 1) {
			document.getElementById('addressText').innerHTML = "Devi inserire almeno 1 carattere";
			validAddress = false;
			return false;
		} else if (des.length > 100) {
			document.getElementById('addressText').innerHTML = "Hai inserito troppi caratteri";
			validAddress = false;
			return false;
		} else {
			document.getElementById('addressText').innerHTML = "";
			validAddress = true;
			return true;
		}
	}
	


function checkMobile(input) {
		var des = input.value.trim();
		if (des.length < 11 && des.length > 10 ) {
			document.getElementById('mobileText').innerHTML = "Il numero deve avere 10 cifre";
			VvalidMobile = false;
			return false;
		} else {
			document.getElementById('mobileText').innerHTML = "";
			validMobile = true;
			return true;
		}
	}
  



function checkPincode(input) {
var des = input.value.trim();
		if (des.length < 6 && des.length > 5 ) {
			document.getElementById('mobileText').innerHTML = "Il pincode deve avere 5 cifre";
			validPincode = false;
			return false;
		} else {
			document.getElementById('mobileText').innerHTML = "";
			validPincode = true;
			return true;
		}
}


function checkPassword(input) {
if (input.value.length < 1) {
			document.getElementById('passwordText').innerHTML = "Minimo 8 caratteri";
			validName = false;
			return false;
		} else if (input.value.length > 20) {
			document.getElementById('passwordText').innerHTML = "Massimo 20 caratteri";
			validName = false;
			return false;
		} else {
			document.getElementById('passwordText').innerHTML = "";
			validName = true;
			return true;
		}
}



	function validateForm() {
		
	    if (!validName) {
	        alert("Controlla il campo nome!!");
	        return false; 
	    }

	    if (!validEmail) {
	        alert("Controlla il campo email!!");
	        return false;
	    }
	    
	    if (!validAddress) {
	        alert("Controlla il campo indirizzo!!");
	        return false;
	    }
	    
	    if (!validMobile) {
	        alert("Controlla il campo numero!!");
	        return false;
	    }
	    
	    if (!validPincode) {
	        alert("Controlla il campo pincode!!");
	        return false;
		}
	    return true;
	}
