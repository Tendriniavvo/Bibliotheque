<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Editeur" %>
<%@ page import="java.util.List" %>

<%
    List<Editeur> editeurs = (List<Editeur>) request.getAttribute("editeurs");
%>

<h1>Ajouter un Livre</h1>

<form action="/livre/save" method="post">
    <div class="form-group">
        <label for="titre">Titre</label>
        <input type="text" id="titre" name="titre" required>
    </div>

    <div class="form-group">
        <label for="isbn">ISBN</label>
        <input type="text" id="isbn" name="isbn" maxlength="20">
    </div>

    <div class="form-group">
        <label for="anneePublication">Année de Publication</label>
        <input type="number" id="anneePublication" name="anneePublication" min="1000" max="9999">
    </div>

    <div class="form-group">
        <label for="resume">Résumé</label>
        <input type="text" id="resume" name="resume" >
    </div>

    <div class="form-group">
        <label for="idEditeur">Éditeur</label>
        <select id="idEditeur" name="idEditeur" required>
            <option value="" disabled selected>Sélectionner un éditeur</option>
            <% for (Editeur editeur : editeurs) { %>
                <option value="<%= editeur.getId() %>"><%= editeur.getNom() %></option>
            <% } %>
        </select>
    </div>

    <button type="submit">Ajouter</button>
    <a href="/livre/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
