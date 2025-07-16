<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="com.bibliotheque.entities.Exemplaire" %>
<%@ page import="com.bibliotheque.entities.TypeEmprunt" %>
<%@ page import="java.util.List" %>
<%
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
    List<Exemplaire> exemplaires = (List<Exemplaire>) request.getAttribute("exemplaires");
    List<TypeEmprunt> typesEmprunt = (List<TypeEmprunt>) request.getAttribute("typesEmprunt");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
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
    input[type="datetime-local"],
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
<h1>Ajouter un Emprunt</h1>
<% if (errorMessage != null && !errorMessage.isEmpty()) { %>
    <div class="message error">
        <%= errorMessage %>
    </div>
<% } %>
<% if (successMessage != null && !successMessage.isEmpty()) { %>
    <div class="message success">
        <%= successMessage %>
    </div>
<% } %>
<form action="/emprunt/save" method="post">
    <div class="form-group">
        <label for="idAdherent">Adhérent <span style="color:red">*</span></label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="" disabled selected>Sélectionner un adhérent</option>
            <% for (Adherent adherent : adherents) { %>
                <option value="<%= adherent.getId() %>"><%= adherent.getNom() %> <%= adherent.getPrenom() %></option>
            <% } %>
        </select>
    </div>
    <div class="form-group">
        <label for="idExemplaire">Exemplaire <span style="color:red">*</span></label>
        <select id="idExemplaire" name="idExemplaire" required>
            <option value="" disabled selected>Sélectionner un exemplaire</option>
            <% for (Exemplaire exemplaire : exemplaires) { %>
                <option value="<%= exemplaire.getId() %>"><%= exemplaire.getLivre() != null ? exemplaire.getLivre().getTitre() : "N/A" %> (Quantité: <%= exemplaire.getQuantite() %>)</option>
            <% } %>
        </select>
    </div>
    <div class="form-group">
        <label for="idTypeEmprunt">Type d'emprunt <span style="color:red">*</span></label>
        <select id="idTypeEmprunt" name="idTypeEmprunt" required>
            <option value="" disabled selected>Sélectionner un type d'emprunt</option>
            <% for (TypeEmprunt type : typesEmprunt) { %>
                <option value="<%= type.getId() %>"><%= type.getNomType() %></option>
            <% } %>
        </select>
    </div>
    <div class="form-group">
        <label for="dateEmprunt">Date d'Emprunt <span style="color:red">*</span></label>
        <input type="datetime-local" id="dateEmprunt" name="dateEmprunt" required>
    </div>
    <div class="form-group">
        <label for="dateRetourPrevue">Date Retour Prévue <span style="color:red">*</span></label>
        <input type="datetime-local" id="dateRetourPrevue" name="dateRetourPrevue" required>
    </div>
    <button type="submit">Ajouter</button>
    <a href="/emprunt/liste"><button type="button" class="back-button">Retour</button></a>
</form>