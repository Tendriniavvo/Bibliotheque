<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Emprunt" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="com.bibliotheque.services.EmpruntService" %>

<%
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
    Map<Integer, String> statuts = (Map<Integer, String>) request.getAttribute("statuts");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
    EmpruntService empruntService = (EmpruntService) request.getAttribute("empruntService");
%>

<h1>Tableau de Bord - Gestion des Emprunts</h1>
<a href="/emprunt/form" class="add-button">Ajouter un Emprunt</a>

<% if (request.getAttribute("successMessage") != null) { %>
    <p style="color: green;"><%= request.getAttribute("successMessage") %></p>
<% } %>
<% if (request.getAttribute("errorMessage") != null) { %>
    <p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>

<style>
    .modal {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0,0,0,0.5);
        justify-content: center;
        align-items: center;
    }
    .modal-content {
        background-color: white;
        padding: 20px;
        border-radius: 5px;
        width: 400px;
        text-align: center;
    }
    .modal-content h2 {
        margin-top: 0;
    }
    .modal-content input[type="date"] {
        margin: 10px 0;
        padding: 8px;
        width: 100%;
    }
    .modal-content button {
        margin: 5px;
        padding: 8px 16px;
    }
</style>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Adhérent</th>
            <th>Exemplaire (Livre)</th>
            <th>Type d'Emprunt</th>
            <th>Date d'Emprunt</th>
            <th>Date Retour Prévue</th>
            <th>Statut</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% if (emprunts != null && !emprunts.isEmpty()) { %>
            <% for (Emprunt emprunt : emprunts) { %>
                <tr>
                    <td><%= emprunt.getId() %></td>
                    <td><%= emprunt.getAdherent() != null ? emprunt.getAdherent().getNom() + " " + emprunt.getAdherent().getPrenom() : "Inconnu" %></td>
                    <td>
                        <%= emprunt.getExemplaire() != null && emprunt.getExemplaire().getLivre() != null 
                            ? emprunt.getExemplaire().getLivre().getTitre() : "Inconnu" %>
                    </td>
                    <td><%= emprunt.getTypeEmprunt() != null ? emprunt.getTypeEmprunt().getNomType() : "Inconnu" %></td>
                    <td><%= emprunt.getDateEmprunt() != null ? formatter.format(emprunt.getDateEmprunt()) : "Non défini" %></td>
                    <td><%= emprunt.getDateRetourPrevue() != null ? formatter.format(emprunt.getDateRetourPrevue()) : "Non défini" %></td>
                    <td><%= statuts != null && statuts.get(emprunt.getId()) != null ? statuts.get(emprunt.getId()) : "En cours" %></td>
                    
                        
                        <% if (statuts == null || !statuts.containsKey(emprunt.getId()) || !"Rendu".equals(statuts.get(emprunt.getId()))) { %>
                        <td class="action-buttons">
                            <button class="edit-button" onclick="openProlongerModal('prolonger-modal-<%= emprunt.getId() %>')">Prolonger</button>
                            <div id="prolonger-modal-<%= emprunt.getId() %>" class="modal">
                                <div class="modal-content">
                                    <h2>Prolonger l'emprunt #<%= emprunt.getId() %></h2>
                                    <form action="/prolongement/save" method="post">
                                        <input type="hidden" name="idEmprunt" value="<%= emprunt.getId() %>">
                                        <label for="dateProlongation-<%= emprunt.getId() %>">Nouvelle date de retour :</label>
                                        <input type="datetime-local" id="dateProlongation-<%= emprunt.getId() %>" name="dateProlongation" required>
                                        <button type="submit">Confirmer</button>
                                        <button type="button" onclick="closeProlongerModal('prolonger-modal-<%= emprunt.getId() %>')">Annuler</button>
                                    </form>
                                </div>
                            </div>
                            <button class="delete-button" onclick="openModal('modal-<%= emprunt.getId() %>')">Rendre</button>
                            <div id="modal-<%= emprunt.getId() %>" class="modal">
                                <div class="modal-content">
                                    <h2>Rendre l'emprunt #<%= emprunt.getId() %></h2>
                                    <form action="/emprunt/rendre" method="post">
                                        <input type="hidden" name="idEmprunt" value="<%= emprunt.getId() %>">
                                        <label for="dateRetour-<%= emprunt.getId() %>">Date de retour :</label>
                                        <input type="date" id="dateRetour-<%= emprunt.getId() %>" name="dateRetour" required>
                                        <button type="submit">Confirmer</button>
                                        <button type="button" onclick="closeModal('modal-<%= emprunt.getId() %>')">Annuler</button>
                                    </form>
                                </div>
                            </div>
                        </td>
                        <% } %>
                    
                </tr>
            <% } %>
        <% } else { %>
            <tr>
                <td colspan="8">Aucun emprunt disponible.</td>
            </tr>
        <% } %>
    </tbody>
</table>

<script>
    function openModal(modalId) {
        document.getElementById(modalId).style.display = 'flex';
    }

    function closeModal(modalId) {
        document.getElementById(modalId).style.display = 'none';
    }
    function openProlongerModal(modalId) {
        document.getElementById(modalId).style.display = 'flex';
    }
    function closeProlongerModal(modalId) {
        document.getElementById(modalId).style.display = 'none';
    }
</script>