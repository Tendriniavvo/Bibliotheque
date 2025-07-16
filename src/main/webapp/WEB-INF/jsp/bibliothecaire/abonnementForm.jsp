<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
%>

<h1>Ajouter un Abonnement</h1>
<form action="/abonnement/save" method="post">
    <div class="form-group">
        <label for="idAdherent">Adhérent</label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="" disabled selected>Sélectionner un adhérent</option>
            <% for (Adherent adherent : adherents ) { %>
                <option value="<%= adherent.getId() %>"><%= adherent.getNom() %></option>
            <% } %>
            
        </select>
    </div>

    <div class="form-group">
        <label for="dateDebut">Date de Début</label>
        <input type="date" id="dateDebut" name="dateDebut" required>
    </div>

    <div class="form-group">
        <label for="dateFin">Date de Fin</label>
        <input type="date" id="dateFin" name="dateFin" required>
    </div>

    <button type="submit">Ajouter</button>
    <a href="/abonnement/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
