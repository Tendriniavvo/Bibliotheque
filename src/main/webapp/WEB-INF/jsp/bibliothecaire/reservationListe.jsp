<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Reservation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    Map<Long, String> statuts = (Map<Long, String>) request.getAttribute("statuts");
%>

<h1>Gestion des Réservations</h1>
<a href="/reservation/form" class="add-button">Ajouter une Réservation</a>

<% if (request.getAttribute("message") != null) { %>
    <p style="color: <%= request.getAttribute("messageType").equals("error") ? "red" : "green" %>;">
        <%= request.getAttribute("message") %>
    </p>
<% } %>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Livre</th>
            <th>Adhérent</th>
            <th>Date Demande</th>
            <th>Date à réserver</th>
            <th>Statut</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% if (reservations != null && !reservations.isEmpty()) { %>
            <% for (Reservation reservation : reservations) { %>
                <tr>
                    <td><%= reservation.getId() %></td>
                    <td><%= reservation.getLivre() != null ? reservation.getLivre().getTitre() : "Non spécifié" %></td>
                    <td><%= reservation.getAdherent() != null ? reservation.getAdherent().getNom() : "Non spécifié" %></td>
                    <td><%= reservation.getDateDemande() != null ? reservation.getDateDemande() : "Non spécifiée" %></td>
                    <td><%= reservation.getDateAReserver() != null ? reservation.getDateAReserver() : "Non spécifiée" %></td>
                    <td><%= statuts != null && statuts.get(reservation.getId()) != null ? statuts.get(reservation.getId()) : "En attente" %></td>
                    <td>
                        <%-- <button onclick="location.href='/reservation/edit?id=<%= reservation.getId() %>'">Modifier</button>
                        <button onclick="if(confirm('Voulez-vous vraiment supprimer cette réservation ?')) location.href='/reservation/delete?id=<%= reservation.getId() %>'">Supprimer</button> --%>
                        <% if (statuts == null || !statuts.containsKey(reservation.getId()) || !"Validée".equals(statuts.get(reservation.getId()))) { %>
                            <form action="/reservation/valider" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= reservation.getId() %>">
                                <button type="submit" onclick="return confirm('Voulez-vous vraiment valider cette réservation ?')">Valider</button>
                            </form>
                        <% } %>
                    </td>
                </tr>
            <% } %>
        <% } else { %>
            <tr>
                <td colspan="7">Aucune réservation disponible.</td>
            </tr>
        <% } %>
    </tbody>
</table>