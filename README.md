# Daggerfall — La Forge

Application de démonstration Spring Boot simulant l'atelier d'un forgeron qui fabrique des épées et des dagues.
La pipeline CI/CD est entièrement écrite en Java avec le SDK Dagger, sans logique d'orchestration dans le YAML.

## Lancer en local

Prérequis : [Dagger CLI](https://docs.dagger.io/install) v0.19.x et Docker.

| Commande | Description |
|---|---|
| `dagger call build` | Compile le projet et les sources de test |
| `dagger call unit-tests` | Lance les tests unitaires (`*Test`) |
| `dagger call integration-tests` | Lance les tests d'intégration (`*IT`, `*Tests`) |
| `dagger call sonar` | Analyse qualité (simulée) |
| `dagger call deploy` | Déploiement (simulé) |
| `dagger call run` | Pipeline complète : build → TU → IT → sonar → deploy |
