# Руководство пользователя для локального поисковика сайтов

## Введение

Это приложение предназначено для осуществления поиска по выбранным сайтам. Оно автоматически анализирует и индексирует содержимое сайтов, предоставляя пользователю возможность поиска по индексированным страницам.

## Технологический стек

1. Java 17: Язык программирования.
2. Spring Boot 2.7.1: Фреймворк для создания веб-приложений.
3. MySQL: Система управления базами данных.
4. Maven: Инструмент для управления проектом.
5. Fork-Join Framework: Фреймворк для параллельного выполнения задач.
6. Apache Lucene Morphology: Библиотека для морфологического анализа текста.
7. JSOUP: Библиотека для парсинга HTML.
8. Lombok: Утилита для автоматической генерации кода.

## Принцип работы

1. Конфигурационный файл содержит адреса сайтов для анализа.
2. Приложение сканирует и индексирует страницы сайтов.
3. Пользователи отправляют запросы через API.
4. Запросы преобразуются в список слов базовой формы для поиска в индексе.
5. Пользователи получают ранжированные и отсортированные результаты.

## Интерфейс

1. Панель управления: статистика сайтов.
2. Управление: инструменты для индексации.
3. Поиск: поле для ввода запросов и выбора сайта.

## Установка и запуск

1. Установите Java 17+ и MySQL.
2. Создайте пустую базу данных search_engine с использованием кодировки utf8mb4.
3. В файл конфигурации settings.xml C:\Users\Username\.m2 добавьте настройки: https://docs.google.com/document/d/1rF_9N1ZbEJwUSxK8VBb1HhqAu7qPYCoM3A4NhiHSY6M/edit?usp=sharing
4. Клонируйте репозиторий: https://github.com/AntonSominskii/SearchengineSkillboxFinalProject.git.
5. Укажите параметры подключения к базе данных в application.yaml.
6. Укажите сайты для индексации.
7. Запустите приложение через Spring Boot.
8. Откройте http://localhost:8080/ в браузере.


# User Guide for Local Site Search Engine

## Introduction

This application is designed to search through selected websites. It automatically analyzes and indexes website content, allowing users to search through indexed pages.

## Technology Stack

1. Java 17: Programming language.
2. Spring Boot 2.7.1: Framework for creating web applications.
3. MySQL: Database management system.
4. Maven: Project management tool.
5. Fork-Join Framework: Framework for parallel task execution.
6. Apache Lucene Morphology: Library for morphological text analysis.
7. JSOUP: Library for HTML parsing.
8. Lombok: Utility for automatic code generation.

## Working Principle

1. The configuration file contains addresses of websites for analysis.
2. The application scans and indexes website pages.
3. Users send requests through the API.
4. Requests are transformed into a list of base form words for searching the index.
5. Users receive ranked and sorted results.

## Interface

1. Dashboard: Site statistics.
2. Management: Indexing tools.
3. Search: Field for inputting queries and choosing a site.

## Installation and Launch

1. Install Java 17+ and MySQL.
2. Create an empty search_engine database using utf8mb4 encoding.
3. To the configuration file settings.xml C:\Users\Username\.m2 add settings: https://docs.google.com/document/d/1rF_9N1ZbEJwUSxK8VBb1HhqAu7qPYCoM3A4NhiHSY6M/edit?usp=sharing
4. Clone the repository: https://github.com/AntonSominskii/SearchengineSkillboxFinalProject.git.
5. Set database connection parameters in application.yaml.
6. Specify websites for indexing.
7. Launch the application through Spring Boot.
8. Open http://localhost:8080/ in a browser.
