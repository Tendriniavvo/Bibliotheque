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
            <td><%= empruntService.getLastStatutForEmprunt(emprunt.getId()) %></td>
            <td class="action-buttons">
                <button class="delete-button" onclick="location.href='/emprunt/prolonger?id=<%= emprunt.getId() %>'">Prolonger</button>
                <button  class="edit-button" onclick="location.href='/emprunt/rendre?idEmprunt=<%= emprunt.getId() %>'">Rendre</button>

            </td>
        </tr>
        <% } %>
    </tbody>
</table>