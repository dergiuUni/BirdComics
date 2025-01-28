package com.birdcomics.GestioneProfili;

public enum RuoloBean {
    GestoreGenerale{
    	@Override
        public String toString() {
            return "GestoreGenerale";
        }
    },
    GestoreMagazzino{
    	@Override
        public String toString() {
            return "GestoreMagazzino";
        }
    },
    RisorseUmane{
    	@Override
        public String toString() {
            return "RisorseUmane";
        }
    },
    GestoreCatalogo{
    	@Override
        public String toString() {
            return "GestoreCatalogo";
        }
    },
    Magazziniere{
    	@Override
        public String toString() {
            return "Magazziniere";
        }
    },
    Spedizioniere{
    	@Override
        public String toString() {
            return "Spedizioniere";
        }
    },
    Assistenza{
    	@Override
        public String toString() {
            return "Assistenza";
        }
    },
    Finanza{
    	@Override
        public String toString() {
            return "Finanza";
        }
    },
    Cliente{
    	@Override
        public String toString() {
            return "Cliente";
        }
    };

    // Metodo per ottenere l'ENUM dalla stringa (per l'input del database)
    public static RuoloBean fromString(String ruolo) {
        return RuoloBean.valueOf(ruolo);
    }
}

