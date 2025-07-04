<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Livre" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="com.bibliotheque.entities.StatutReservation" %>
<%@ page import="java.util.List" %>

<%
    Adherent profil = (Adherent) request.getAttribute("profil");
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
    List<Livre> livres = (List<Livre>) request.getAttribute("livres");
    List<StatutReservation> statuts = (List<StatutReservation>) request.getAttribute("statuts");
%>

<h1>Ajouter une Réservation</h1>

<form action="reservation/save" method="post">

    <div class="form-group">
        <label for="idLivre">Livre <span style="color:red">*</span></label>
        <select id="idLivre" name="idLivre" required>
            <option value="" disabled selected>Sélectionner un livre</option>
            <% for (Livre livre : livres) { %>
                <option value="<%= livre.getId() %>"><%= livre.getTitre() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="idAdherent">Adhérent <span style="color:red">*</span></label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="" disabled selected>Sélectionner un adhérent</option>
            <% for (Adherent adherent : adherents) { %>
                <option value="<%= adherent.getId() %>"><%= adherent.getNom() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="idStatut">Statut <span style="color:red">*</span></label>
        <select id="idStatut" name="idStatut" required>
            <option value="" disabled selected>Sélectionner un statut</option>
            <% for (StatutReservation statut : statuts) { %>
                <option value="<%= statut.getId() %>"><%= statut.getCodeStatut() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="dateExpiration">Date d'expiration <span style="color:red">*</span></label>
        <input type="date" id="dateExpiration" name="dateExpiration" required />
    </div>

    <button type="submit">Enregistrer</button>
    <a href="/reservation/liste"><button type="button">Retour</button></a>

</form>
