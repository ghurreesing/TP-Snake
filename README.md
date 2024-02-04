# TP-Snake

**Instructions d'installation :**

 **Cloner le repository :**
```bash 
git clone https://github.com/ghurreesing/TP-Snake.git 
```

**Importer le projet dans Eclipse :**

1. Ouvrez Eclipse.
2. Sélectionnez `File` -> `Import...` -> `General` -> `Existing Projects into Workspace`.
3. Choisissez le répertoire du projet que vous venez de cloner.
4. Cliquez sur "Finish".

**Configurer le chemin de la bibliothèque JavaFX :**

1. Sélectionnez `Build Path` -> `Configure Build Path...`.
2. Dans l'onglet `Libraries`, assurez-vous que la librairie JavaFX est présente.
3. Si elle n'est pas là, ajoutez les JARs manuellement en cliquant sur `Add External JARs...` et en sélectionnant le chemin vers le dossier `lib` de la librairie JavaFX.

**Configurer les arguments VM dans Eclipse :**

1. Cliquez avec le bouton droit sur votre projet dans l'explorateur de projets.
2. Sélectionnez `Run As` -> `Run Configurations` -> `Arguments`.
3. Dans la section "VM Arguments", ajoutez les arguments nécessaires pour spécifier le chemin du module JavaFX.
   ```bash
   --module-path "chemin/vers/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
   ```
Assurez-vous de remplacer "chemin/vers/javafx-sdk" par le chemin réel de votre installation JavaFX SDK.

4. Cliquez sur "Apply" pour enregistrer les configurations.

**Comment lancer le jeu :**
Exécutez la classe `SnakeApp`.
