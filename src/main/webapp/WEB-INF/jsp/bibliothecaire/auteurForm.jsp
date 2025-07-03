<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1>Ajouter un Auteur</h1>

<form action="/auteur/save" method="post">
    <div class="form-group">
        <label for="nom">Nom <span style="color: red">*</span></label>
        <input type="text" id="nom" name="nom" required>
    </div>

    <div class="form-group">
        <label for="prenom">Prénom</label>
        <input type="text" id="prenom" name="prenom">
    </div>

    <button type="submit">Ajouter</button>
    <a href="/auteur/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
