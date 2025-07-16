<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Livre" %>
<%@ page import="java.util.List" %>

<%
    List<Livre> livres = (List<Livre>) request.getAttribute("livres");
%>

<h1>Ajouter un Exemplaire</h1>

<form action="/exemplaire/save" method="post">
    <div class="form-group">
        <label for="idLivre">Livre</label>
        <select id="idLivre" name="idLivre" required>
            <option value="" disabled selected>Sélectionner un livre</option>
            <% for (Livre livre : livres) { %>
                <option value="<%= livre.getId() %>"><%= livre.getTitre() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="quantite">Quantité</label>
        <input type="number" id="quantite" name="quantite" min="1" required>
    </div>

    <button type="submit">Ajouter</button>
    <a href="/exemplaire/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
