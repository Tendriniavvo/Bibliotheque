<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Livre" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    Adherent profil = (Adherent) request.getAttribute("profil");
    List<Livre> livres = (List<Livre>) request.getAttribute("livres");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
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

<h1>Ajouter une Réservation</h1>

<% if (message != null && !message.isEmpty()) { %>
    <div class="message <%= "success".equals(messageType) ? "success" : "error" %>">
        <%= message %>
    </div>
<% } %>

<form action="/reservation/save" method="post">

    <input type="hidden" name="idAdherent" value="<%= profil != null ? profil.getId() : "" %>" />

    <div class="form-group">
        <label>Adhérent</label>
        <p><%= profil != null ? profil.getNom() + " " + profil.getPrenom() : "Non connecté" %></p>
    </div>

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
        <label for="dateAReserver">Date à réserver <span style="color:red">*</span></label>
        <input type="date" id="dateAReserver" name="dateAReserver" required />
    </div>

    <button type="submit">Enregistrer</button>
    <a href="/reservation/liste"><button type="button" class="back-button">Retour</button></a>

</form>

<script>
    document.getElementById('dateAReserver').addEventListener('change', function() {
        const selectedDate = new Date(this.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        if (selectedDate < today) {
            alert("La date de réservation doit être aujourd'hui ou dans le futur.");
            this.value = '';
        }
    });
</script>