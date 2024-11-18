#!/bin/bash

# Récupérer la branche actuelle et la stocker dans une variable
current_branch=$(git rev-parse --abbrev-ref HEAD)

# Demander à l'utilisateur d'entrer un message de commit
echo "Entrez votre message de commit :"
read commit_message

# Exécuter les commandes Git
git restore target/
git clean -fd
git add *
git commit -m "$commit_message"

# Afficher le message de confirmation
echo "Les changements sont prêts à être poussés sur la branche '$current_branch'."
