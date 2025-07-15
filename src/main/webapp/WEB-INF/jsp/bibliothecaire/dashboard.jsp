<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<h1>Tableau de bord - Bibliothèque</h1>
<div style="display: flex; gap: 30px; flex-wrap: wrap;">
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Adhérents</h2>
        <p style="font-size: 2em; color: #3498db;">${nbAdherents}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Livres</h2>
        <p style="font-size: 2em; color: #27ae60;">${nbLivres}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Exemplaires</h2>
        <p style="font-size: 2em; color: #e67e22;">${nbExemplaires}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Emprunts en cours</h2>
        <p style="font-size: 2em; color: #9b59b6;">${nbEmpruntsEnCours}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Pénalités actives</h2>
        <p style="font-size: 2em; color: #c0392b;">${nbPenalitesActives}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Abonnements actifs</h2>
        <p style="font-size: 2em; color: #16a085;">${nbAbonnementsActifs}</p>
    </div>
</div> 