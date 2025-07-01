<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter un département</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h2>Ajouter un département</h2>
    <form method="post" action="saveDept">
        <div>
            <label>Nom:</label>
            <input type="text" name="nom" required/>
        </div>
        <input type="submit" value="Enregistrer">
    </form>
    <a href="listeDept">Retour à la liste</a>
</body>
</html>
