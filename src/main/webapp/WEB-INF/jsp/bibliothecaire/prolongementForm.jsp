<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Emprunt" %>
<%@ page import="java.util.List" %>

<%
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
%>

<h1>Ajouter un Prolongement</h1>

<form action="/prolongement/save" method="post">
    <div class="form-group">
        <label for="idEmprunt">Emprunt</label>
        <select id="idEmprunt" name="idEmprunt" required>
            <option value="" disabled selected>Sélectionner un emprunt</option>
            <% for (Emprunt e : emprunts) { %>
                <option value="<%= e.getId() %>">Emprunt #<%= e.getId() %> - <%= e.getAdherent().getNom() %> <%= e.getAdherent().getPrenom() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="dateFin">Nouvelle Date de Fin</label>
        <input type="datetime-local" id="dateFin" name="dateFin" required>
    </div>

    <div class="form-group">
        <label for="dateProlongement">Date du Prolongement</label>
        <input type="datetime-local" id="dateProlongement" name="dateProlongement" required>
    </div>

    <button type="submit">Ajouter</button>
    <a href="/prolongement/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
