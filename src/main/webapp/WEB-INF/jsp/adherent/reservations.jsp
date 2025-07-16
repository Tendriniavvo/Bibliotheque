<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Reservation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
%>
<h1>Mes Réservations</h1>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
        <tr>
            <th>ID</th>
            <th>Livre</th>
            <th>Date de réservation</th>
            <th>Statut</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <% if (reservations != null && !reservations.isEmpty()) { %>
            <% for (Reservation reservation : reservations) { %>
                <tr>
                    <td><%= reservation.getId() %></td>
                    <td><%= reservation.getLivre() != null ? reservation.getLivre().getTitre() : "N/A" %></td>
                    <td><%= reservation.getDateAReserver() != null ? formatter.format(reservation.getDateAReserver()) : "" %></td>
                    <td><%= request.getAttribute("statuts") != null && ((java.util.Map<Integer, String>)request.getAttribute("statuts")).get(reservation.getId()) != null ? ((java.util.Map<Integer, String>)request.getAttribute("statuts")).get(reservation.getId()) : "En attente" %></td>
                    <td><a href="/adherent/reservation/detail?id=<%= reservation.getId() %>">Voir</a></td>
                </tr>
            <% } %>
        <% } else { %>
            <tr><td colspan="5">Aucune réservation trouvée.</td></tr>
        <% } %>
    </tbody>
</table> 