<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Livre" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="com.bibliotheque.entities.Reservation" %>
<%@ page import="com.bibliotheque.entities.StatutReservation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
    List<StatutReservation> statuts = (List<StatutReservation>) request.getAttribute("statuts");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    Reservation reservation = (Reservation) request.getAttribute("reservation");
    List<Livre> livres = (List<Livre>) request.getAttribute("livres");
%>

<style>
    .form-group {
        margin-bottom: 15px;
    }
    label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: #2c3e50;
    }
    input[type="date"],
    select {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 5px;
        box-sizing: border-box;
        margin-bottom: 10px;
        font-size: 14px;
    }
    button {
        padding: 10px 20px;
        background-color: #3498db;
        color: #fff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s;
    }
    button:hover {
        background-color: #2980b9;
    }
    .back-button {
        background-color: #7f8c8d;
        margin-left: 10px;
    }
    .back-button:hover {
        background-color: #6c7a89;
    }
    .message {
        padding: 10px;
        margin-bottom: 20px;
        border-radius: 5px;
        font-size: 14px;
        text-align: center;
    }
    .message.success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }
    .message.error {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }
</style>

<h1><%= (reservation != null && reservation.getId() != null) ? "Modifier une Réservation" : "Ajouter une Réservation" %></h1>

<% if (message != null && !message.isEmpty()) { %>
    <div class="message <%= "success".equals(messageType) ? "success" : "error" %>">
        <%= message %>
    </div>
<% } %>

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
                    <%= adherent.getNom() %> <%= adherent.getPrenom() %>
                </option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="dateAReserver">Date à réserver <span style="color:red">*</span></label>
        <input type="date" id="dateAReserver" name="dateAReserver" required
               value="<%= (reservation != null && reservation.getDateAReserver() != null) ? 
               reservation.getDateAReserver().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "" %>"/>
    </div>

    <button type="submit">Enregistrer</button>
    <a href="/reservation/liste"><button type="button" class="back-button">Retour</button></a>

</form>