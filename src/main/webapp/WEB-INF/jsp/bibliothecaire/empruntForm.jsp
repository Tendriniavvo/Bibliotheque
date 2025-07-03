<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="com.bibliotheque.entities.Exemplaire" %>
<%@ page import="com.bibliotheque.entities.TypeEmprunt" %>
<%@ page import="java.util.List" %>

<%
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
    List<Exemplaire> exemplaires = (List<Exemplaire>) request.getAttribute("exemplaires");
    List<TypeEmprunt> typesEmprunt = (List<TypeEmprunt>) request.getAttribute("typesEmprunt");
%>

<h1>Ajouter un Emprunt</h1>

<form action="/emprunt/save" method="post">
    <div class="form-group">
        <label for="idAdherent">Adhérent</label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="" disabled selected>Sélectionner un adhérent</option>
            <% for (Adherent adherent : adherents) { %>
                <option value="<%= adherent.getId() %>"><%= adherent.getNom() %> <%= adherent.getPrenom() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="idExemplaire">Exemplaire</label>
        <select id="idExemplaire" name="idExemplaire" required>
            <option value="" disabled selected>Sélectionner un exemplaire</option>
            <% for (Exemplaire exemplaire : exemplaires) { %>
                <option value="<%= exemplaire.getId() %>"><%= exemplaire.getLivre() != null ? exemplaire.getLivre().getTitre() : "N/A" %> (Quantité: <%= exemplaire.getQuantite() %>)</option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="idTypeEmprunt">Type d'emprunt</label>
        <select id="idTypeEmprunt" name="idTypeEmprunt" required>
            <option value="" disabled selected>Sélectionner un type d'emprunt</option>
            <% for (TypeEmprunt type : typesEmprunt) { %>
                <option value="<%= type.getId() %>"><%= type.getNomType() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="dateEmprunt">Date d'Emprunt</label>
        <input type="datetime-local" id="dateEmprunt" name="dateEmprunt" required>
    </div>

    <div class="form-group">
        <label for="dateRetourPrevue">Date Retour Prévue</label>
        <input type="datetime-local" id="dateRetourPrevue" name="dateRetourPrevue" required>
    </div>

    <button type="submit">Ajouter</button>
    <a href="/emprunt/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
