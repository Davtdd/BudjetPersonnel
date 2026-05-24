-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : dim. 24 mai 2026 à 20:24
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestion_budget`
--

-- --------------------------------------------------------

--
-- Structure de la table `transaction`
--

CREATE TABLE `transaction` (
  `id` int(11) NOT NULL,
  `utilisateur_id` int(11) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `montant` double DEFAULT NULL,
  `categorie` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `transaction`
--

INSERT INTO `transaction` (`id`, `utilisateur_id`, `type`, `montant`, `categorie`, `date`, `description`) VALUES
(1, 5, 'REVENU', 5000, 'Salaire', '2026-04-03', NULL),
(2, 5, 'DEPENSE', 1200, 'Divertissement', '2026-04-03', NULL),
(3, 5, 'DEPENSE', 1500, 'Logement', '2026-04-03', NULL),
(4, 5, 'DEPENSE', 100, 'Transport', '2026-04-03', NULL),
(5, 5, 'DEPENSE', 580, 'Courses', '2026-04-03', NULL),
(6, 5, 'DEPENSE', 500, 'Autres', '2026-04-03', NULL),
(7, 6, 'REVENU', 1350, 'Salaire', '2026-04-03', NULL),
(8, 6, 'DEPENSE', 500, 'Logement', '2026-04-03', NULL),
(9, 6, 'DEPENSE', 200, 'Courses', '2026-04-03', NULL),
(10, 6, 'DEPENSE', 100, 'Transport', '2026-04-03', NULL),
(13, 7, 'DEPENSE', 50000, 'Salaire', '2026-04-07', NULL),
(14, 7, 'REVENU', 500000, 'Salaire', '2026-04-07', NULL),
(15, 7, 'DEPENSE', 1500, 'Logement', '2026-04-07', NULL),
(16, 7, 'DEPENSE', 15000, 'Salaire', '2026-04-07', NULL),
(17, 7, 'DEPENSE', 10000, 'Salaire', '2026-04-07', NULL),
(18, 5, 'REVENU', 800, 'Salaire', '2026-04-22', NULL),
(19, 5, 'DEPENSE', 1500, 'Logement', '2026-04-22', NULL),
(20, 5, 'DEPENSE', 100, 'Transport', '2026-04-22', NULL),
(21, 5, 'DEPENSE', 150, 'Courses', '2026-04-22', NULL),
(22, 5, 'DEPENSE', 50, 'Divertissement', '2026-04-22', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `motDePasseHash` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `nom`, `prenom`, `email`, `motDePasseHash`) VALUES
(5, 'Freda', 'Tir', 'freda@gmail.com', 'c8967dedf7f7bba226fbe05c78cdbf13a58b4608bd946866d6277fe4e2c9a84d'),
(6, 'Nkoukou', 'Theo', 'theo@gmail.com', '165f7ddfd1b18b26b4b6a7319d0884c72d7c218be050246a99b7f50531becab1'),
(7, 'madame tran', 'indoze', 'tran@gmail.com', '275016b4e748d64d5b38852a9f12ec857a7a6adcb705b5b298ebdb404f9381f7');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`),
  ADD KEY `utilisateur_id` (`utilisateur_id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
