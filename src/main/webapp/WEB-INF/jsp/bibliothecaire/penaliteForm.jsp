<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="com.bibliotheque.entities.Emprunt" %>
<%@ page import="java.util.List" %>

<%
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
%>

<h1>Ajouter une Pénalité</h1>
<form action="/penalite/save" method="post">
    <div class="form-group">
        <label for="idAdherent">Adhérent</label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="" disabled selected>Sélectionner un adhérent</option>
            <% for (Adherent adherent : adherents ) { %>
                <option value="<%= adherent.getId() %>"><%= adherent.getNom() %> <%= adherent.getPrenom() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="idEmprunt">Emprunt</label>
        <select id="idEmprunt" name="idEmprunt" required>
            <option value="" disabled selected>Sélectionner un emprunt</option>
            <% for (Emprunt emprunt : emprunts ) { %>
                <option value="<%= emprunt.getId() %>"><%= emprunt.getAdherent().getNom() %> <%= emprunt.getTypeEmprunt().getNomType() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="dateDebut">Date de Début</label>
        <input type="date" id="dateDebut" name="dateDebut" required>
    </div>

    <div class="form-group">
        <label for="jour">Nombre de Jours</label>
        <input type="number" id="jour" name="jour" min="1" required>
    </div>

    <div class="form-group">
        <label for="raison">Raison</label>
        <input type="text" id="raison" name="raison">
    </div>

    <button type="submit">Ajouter</button>
    <a href="/penalite/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
