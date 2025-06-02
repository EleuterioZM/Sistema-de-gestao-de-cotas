-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 31, 2025 at 12:39 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestao_cotas`
--

-- --------------------------------------------------------

--
-- Table structure for table `cotas`
--

CREATE TABLE `cotas` (
  `id` int(11) NOT NULL,
  `data_criacao` datetime(6) DEFAULT NULL,
  `data_vencimento` datetime(6) DEFAULT NULL,
  `descricao` text DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `referencia_pagamento` varchar(255) DEFAULT NULL,
  `status` enum('pendente','paga','vencida') DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `entidade_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `entidades`
--

CREATE TABLE `entidades` (
  `id` int(11) NOT NULL,
  `data_criacao` datetime(6) DEFAULT NULL,
  `descricao` text DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notificacoes`
--

CREATE TABLE `notificacoes` (
  `id` int(11) NOT NULL,
  `data_envio` datetime(6) DEFAULT NULL,
  `lida` bit(1) DEFAULT NULL,
  `mensagem` varchar(255) DEFAULT NULL,
  `tipo` enum('sistema','alerta','informativo') DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `entidade_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pagamentos`
--

CREATE TABLE `pagamentos` (
  `id` int(11) NOT NULL,
  `comprovativo_url` varchar(255) DEFAULT NULL,
  `data_pagamento` datetime(6) DEFAULT NULL,
  `estado` enum('confirmado','pendente','falhado') DEFAULT NULL,
  `metodo_pagamento` varchar(255) DEFAULT NULL,
  `referencia_bancaria` varchar(255) DEFAULT NULL,
  `valor_pago` double DEFAULT NULL,
  `cota_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `relatorios`
--

CREATE TABLE `relatorios` (
  `id` int(11) NOT NULL,
  `conteudo` text DEFAULT NULL,
  `data_geracao` datetime(6) DEFAULT NULL,
  `tipo` enum('financeiro','pagamento','cotas') DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `entidade_id` int(11) DEFAULT NULL,
  `gerado_por` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `ativo` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `perfil` enum('admin','operador','visualizador') DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL,
  `ultimo_login` datetime(6) DEFAULT NULL,
  `entidade_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`id`, `ativo`, `email`, `nome`, `perfil`, `senha`, `ultimo_login`, `entidade_id`) VALUES
(1, NULL, 'juniormabecuane7@gmail.com', 'Eleuterio Zacarias Mabecuane', NULL, '$2a$10$Nqy2P6zZ0ZpBs7Za28dxsOfvhFglHUevO37CZN/7tgMOgc6maPO6y', NULL, NULL),
(2, NULL, 'juniormabecuane7@gmail.com', 'teste', NULL, '$2a$10$NO0mDBQKfb5uPwKQ59BSLuv5b2FjeFGSvqYKZCCUs13C6UQkfLWGC', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cotas`
--
ALTER TABLE `cotas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhb5idq81r22du1amqb9tgohgb` (`entidade_id`);

--
-- Indexes for table `entidades`
--
ALTER TABLE `entidades`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notificacoes`
--
ALTER TABLE `notificacoes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKj1rwbxuwhjh890emgn3dwnhee` (`entidade_id`);

--
-- Indexes for table `pagamentos`
--
ALTER TABLE `pagamentos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkm5lg1uydci67qn98cw12efb2` (`cota_id`);

--
-- Indexes for table `relatorios`
--
ALTER TABLE `relatorios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK1g01uqtck6ha79tbn2wayw59g` (`entidade_id`),
  ADD KEY `FK37hhsimk46o47nue4x7cbbgon` (`gerado_por`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK52ex1mnaprclqbqifgj7lgp68` (`entidade_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cotas`
--
ALTER TABLE `cotas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `entidades`
--
ALTER TABLE `entidades`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notificacoes`
--
ALTER TABLE `notificacoes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pagamentos`
--
ALTER TABLE `pagamentos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `relatorios`
--
ALTER TABLE `relatorios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cotas`
--
ALTER TABLE `cotas`
  ADD CONSTRAINT `FKhb5idq81r22du1amqb9tgohgb` FOREIGN KEY (`entidade_id`) REFERENCES `entidades` (`id`);

--
-- Constraints for table `notificacoes`
--
ALTER TABLE `notificacoes`
  ADD CONSTRAINT `FKj1rwbxuwhjh890emgn3dwnhee` FOREIGN KEY (`entidade_id`) REFERENCES `entidades` (`id`);

--
-- Constraints for table `pagamentos`
--
ALTER TABLE `pagamentos`
  ADD CONSTRAINT `FKkm5lg1uydci67qn98cw12efb2` FOREIGN KEY (`cota_id`) REFERENCES `cotas` (`id`);

--
-- Constraints for table `relatorios`
--
ALTER TABLE `relatorios`
  ADD CONSTRAINT `FK1g01uqtck6ha79tbn2wayw59g` FOREIGN KEY (`entidade_id`) REFERENCES `entidades` (`id`),
  ADD CONSTRAINT `FK37hhsimk46o47nue4x7cbbgon` FOREIGN KEY (`gerado_por`) REFERENCES `usuarios` (`id`);

--
-- Constraints for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `FK52ex1mnaprclqbqifgj7lgp68` FOREIGN KEY (`entidade_id`) REFERENCES `entidades` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
