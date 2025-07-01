package com.bibliotheque.models;

public class RegisterForm {
    private String email;
    private String motDePasseHash;
    private String nom;
    private String prenom;
    private String dateNaissance; // au format yyyy-MM-dd
    private Integer idProfil;

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String motDePasseHash) { this.motDePasseHash = motDePasseHash; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public Integer getIdProfil() { return idProfil; }
    public void setIdProfil(Integer idProfil) { this.idProfil = idProfil; }
}
