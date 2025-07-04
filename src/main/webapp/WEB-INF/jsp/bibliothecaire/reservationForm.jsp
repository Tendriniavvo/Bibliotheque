<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Livre" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="com.bibliotheque.entities.Reservation" %>
<%@ page import="com.bibliotheque.entities.StatutReservation" %>
<%@ page import="java.util.List" %>

<%
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
    List<StatutReservation> statuts = (List<StatutReservation>) request.getAttribute("statuts");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType"); // 👈 nécessaire pour éviter l’erreur
    Reservation reservation = (Reservation) request.getAttribute("reservation");
%>

<% if (message != null && !message.isEmpty()) { %>
    <div class="message <%= "success".equals(messageType) ? "success" : "error" %>">
        <%= message %>
    </div>
<% } %>

<h1><%= (reservation != null && reservation.getId() != null) ? "Modifier une Réservation" : "Ajouter une Réservation" %></h1>

<form action="<%= (reservation != null && reservation.getId() != null) ? "/reservation/update" : "/reservation/save" %>" method="post">

    <% if (reservation != null && reservation.getId() != null) { %>
        <input type="hidden" name="id" value="<%= reservation.getId() %>" />
    <% } %>

    <div class="form-group">
        <label for="idLivre">Livre <span style="color:red">*</span></label>
        <select id="idLivre" name="idLivre" required>
            <option value="" disabled <%= (reservation == null) ? "selected" : "" %>>Sélectionner un livre</option>
            <% for (Livre livre : livres) { %>
                <option value="<%= livre.getId() %>"
                    <%= (reservation != null && reservation.getLivre() != null && livre.getId().equals(reservation.getLivre().getId())) ? "selected" : "" %>>
                    <%= livre.getTitre() %>
                </option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="idAdherent">Adhérent <span style="color:red">*</span></label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="" disabled <%= (reservation == null) ? "selected" : "" %>>Sélectionner un adhérent</option>
            <% for (Adherent adherent : adherents) { %>
                <option value="<%= adherent.getId() %>"
                    <%= (reservation != null && reservation.getAdherent() != null && adherent.getId().equals(reservation.getAdherent().getId())) ? "selected" : "" %>>
                    <%= adherent.getNom() %>
                </option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="idStatut">Statut <span style="color:red">*</span></label>
        <select id="idStatut" name="idStatut" required>
            <option value="" disabled <%= (reservation == null) ? "selected" : "" %>>Sélectionner un statut</option>
            <% for (StatutReservation statut : statuts) { %>
                <option value="<%= statut.getId() %>"
                    <%= (reservation != null && reservation.getStatut() != null && statut.getId().equals(reservation.getStatut().getId())) ? "selected" : "" %>>
                    <%= statut.getCodeStatut() %>
                </option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="dateExpiration">Date d'expiration <span style="color:red">*</span></label>
        <input type="date" id="dateExpiration" name="dateExpiration" required
               value="<%= (reservation != null && reservation.getDateExpiration() != null) ? reservation.getDateExpiration() : "" %>"/>
    </div>

    <button type="submit">Enregistrer</button>
    <a href="/reservation/liste"><button type="button">Retour</button></a>

</form>
