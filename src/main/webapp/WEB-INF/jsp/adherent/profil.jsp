<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="com.bibliotheque.entities.ProfilsAdherent" %>
<%@ page import="java.util.List" %>
<%
    Adherent adherent = (Adherent) request.getAttribute("adherent");
    ProfilsAdherent profil = (ProfilsAdherent) request.getAttribute("profil");
    Integer quota = (Integer) request.getAttribute("quota");
    List<String> penalites = (List<String>) request.getAttribute("penalites");
    Boolean abonnementActif = (Boolean) request.getAttribute("abonnementActif");
%>
<h1>Mon Profil</h1>
<table border="1" cellpadding="8" cellspacing="0">
    <tr><th>Nom</th><td><%= adherent != null ? adherent.getNom() : "" %></td></tr>
    <tr><th>Prénom</th><td><%= adherent != null ? adherent.getPrenom() : "" %></td></tr>
    <tr><th>Email</th><td><%= adherent != null ? adherent.getIdUtilisateur() : "" %></td></tr>
    <tr><th>Profil</th><td><%= profil != null ? profil.getNomProfil() : "" %></td></tr>
    <tr><th>Quota</th><td><%= quota != null ? quota : "" %></td></tr>
    <tr><th>Pénalités actives</th><td><%= penalites != null ? penalites.size() : 0 %></td></tr>
    <tr><th>Abonnement actif</th><td><%= abonnementActif != null && abonnementActif ? "Oui" : "Non" %></td></tr>
</table> 