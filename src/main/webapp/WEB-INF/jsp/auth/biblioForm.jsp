<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Insertion Bibliothécaire</title>
    <style>
        body { font-family: Arial; background: #f2f2f2; padding: 20px; }
        form { background: white; padding: 20px; max-width: 400px; margin: auto; border-radius: 8px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input { width: 100%; padding: 8px; }
        button { width: 100%; padding: 10px; }
    </style>
</head>
<body>

<h2 style="text-align:center;">Ajouter un Bibliothécaire</h2>

<form action="/ajouterBibliothecaire" method="post">
    <div class="form-group">
        <label for="idUtilisateur">ID Utilisateur</label>
        <input type="number" id="idUtilisateur" name="idUtilisateur" required>
    </div>

    <div class="form-group">
        <label for="nom">Nom</label>
        <input type="text" id="nom" name="nom" required>
    </div>

    <div class="form-group">
        <label for="prenom">Prénom</label>
        <input type="text" id="prenom" name="prenom" required>
    </div>

    <button type="submit">Enregistrer</button>
</form>

</body>
</html>
