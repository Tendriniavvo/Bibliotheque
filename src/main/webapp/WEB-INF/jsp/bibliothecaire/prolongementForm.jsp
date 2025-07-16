<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Emprunt" %>
<%@ page import="java.util.List" %>
<%
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
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
<h1>Ajouter un Prolongement</h1>
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
<form action="/prolongement/save" method="post">
    <div class="form-group">
        <label for="idEmprunt">Emprunt <span style="color:red">*</span></label>
        <select id="idEmprunt" name="idEmprunt" required>
            <option value="" disabled selected>Sélectionner un emprunt</option>
            <% for (Emprunt emprunt : emprunts) { %>
                <option value="<%= emprunt.getId() %>">
                    Emprunt #<%= emprunt.getId() %> - <%= emprunt.getAdherent().getNom() %> <%= emprunt.getAdherent().getPrenom() %>
                    (<%= emprunt.getExemplaire().getLivre() != null ? emprunt.getExemplaire().getLivre().getTitre() : "N/A" %>)
                </option>
            <% } %>
        </select>
    </div>
    <div class="form-group">
        <label for="dateFin">Nouvelle Date de Fin <span style="color:red">*</span></label>
        <input type="datetime-local" id="dateFin" name="dateFin" required>
    </div>
    <button type="submit">Ajouter</button>
    <a href="/prolongement/liste"><button type="button" class="back-button">Retour</button></a>
</form>