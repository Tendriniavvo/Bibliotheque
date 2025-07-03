<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1>Ajouter un Éditeur</h1>

<form action="/editeur/save" method="post">
    <div class="form-group">
        <label for="nom">Nom <span style="color: red">*</span></label>
        <input type="text" id="nom" name="nom" required placeholder="Nom de l'éditeur">
    </div>

    <button type="submit">Ajouter</button>
    <a href="/editeur/liste">
        <button type="button" class="back-button">Retour</button>
    </a>
</form>
