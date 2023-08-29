# ApiAutomation

L'application utilise les API REST de WSO2 pour les opérations de gestion des APIs, ainsi que les API REST de GitHub pour accéder au contenu des fichiers Swagger dans votre repo GitHub.

Configuration du Webhook : Vous configurez un Webhook dans votre repo GitHub afin de détecter automatiquement les changements dans les fichiers contenant les URLs de Swagger. Lorsqu'un changement est détecté, le Webhook déclenche une action.

Pipeline Jenkins : Vous créez une pipeline Jenkins qui est déclenchée par le Webhook lorsqu'un changement est détecté dans le repo GitHub. La pipeline Jenkins se charge de construire le projet Java, d'exécuter les tests automatisés et de déployer les APIs en utilisant les API REST de WSO2.

Récupération des URLs d'API depuis le repo GitHub : L'application Java utilise les API REST de GitHub pour récupérer le contenu des fichiers Swagger qui contiennent les URLs des APIs. Elle effectue un appel API pour obtenir ces informations depuis le repo GitHub.

Tests et validation de l'application : Une fois que l'application Java est développée, vous effectuez des tests approfondis pour vous assurer que toutes les fonctionnalités fonctionnent correctement. Cela inclut la vérification de l'importation et du déploiement des APIs, ainsi que l'exécution des tests automatisés pour garantir la qualité des intégrations.

Suivi des évolutions et gouvernance des APIs : L'application Java permet également de suivre les évolutions des APIs et les politiques de gestion associées. Vous pouvez visualiser les modifications apportées aux APIs et générer des rapports détaillés pour faciliter la gouvernance des APIs.

Publication automatique et suivi : Une fois que les APIs sont testées et validées, l'application Java utilise les API REST de GitHub pour mettre à jour l'URL du Swagger dans une release du repo GitHub. Cela permet aux développeurs et aux utilisateurs d'accéder facilement à la documentation de l'API à jour.

Ces étapes décrivent comment votre projet Java s'intègre avec Jenkins, le Webhook et les API REST de GitHub pour automatiser le déploiement, les tests, le suivi et la publication des APIs.
