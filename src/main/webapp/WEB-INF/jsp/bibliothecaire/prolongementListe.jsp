<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Prolongement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Map" %>

<%
    List<Prolongement> prolongements = (List<Prolongement>) request.getAttribute("prolongements");
    Map<Integer, String> statuts = (Map<Integer, String>) request.getAttribute("statuts");
    Map<Integer, List<com.bibliotheque.entities.MvtProlongement>> mouvements = (Map<Integer, List<com.bibliotheque.entities.MvtProlongement>>) request.getAttribute("mouvements");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
%>

<h1>Tableau de Bord - Gestion des Prolongements</h1>
<a href="/prolongement/form" class="add-button">Ajouter un Prolongement</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>ID Emprunt</th>
            <th>Adherent</th>
            <th>Date Fin</th>
            <th>Date de Prolongement</th>
            <th>Statut</th>
            <th>Mouvements</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Prolongement p : prolongements) { %>
        <tr>
            <td><%= p.getId() %></td>
            <td><%= p.getEmprunt() != null ? p.getEmprunt().getId() : "" %></td>
            <td><%= p.getEmprunt() != null ? p.getEmprunt().getAdherent().getNom() : "" %></td>
            <td><%= p.getDateFin() != null ? formatter.format(p.getDateFin()) : "" %></td>
            <td><%= p.getDateProlongement() != null ? formatter.format(p.getDateProlongement()) : "" %></td>
            <td><%= statuts != null && statuts.get(p.getId()) != null ? statuts.get(p.getId()) : "En attente" %></td>
            <td>
                <% if (mouvements != null && mouvements.get(p.getId()) != null) { %>
                    <% for (com.bibliotheque.entities.MvtProlongement m : mouvements.get(p.getId())) { %>
                        <div><%= m.getDateMouvement() %> : <%= m.getStatutNouveau().getCodeStatut() %></div>
                    <% } %>
                <% } %>
            </td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/prolongement/edit?id=<%= p.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer ce prolongement ?')) location.href='/prolongement/delete?id=<%= p.getId() %>'">Supprimer</button>
                <form action="/prolongement/valider" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= p.getId() %>"/>
                    <button type="submit" class="validate-button">Valider</button>
                </form>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
