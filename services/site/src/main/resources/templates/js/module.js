var job4j = angular.module("job4j", ['ngRoute', 'angularUtils.directives.dirPagination']);
var version = '19.10.2018';

job4j.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.when('/vacancies', {
            templateUrl: './vacancy/vacancies.html?=' + version,
            controller: 'VacanciesActiveController'
        }).when('/vacancies/:key', {
            templateUrl: './vacancy/vacancies.html?=' + version,
            controller: 'VacanciesActiveController'
        }).when('/resume', {
            templateUrl: './resume/resume.html?=' + version,
            controller: 'ResumeController'
        }).when('/cv/:key', {
            templateUrl: './settings/cv.html?=' + version,
            controller: 'CandidateController'
        }).when('/index', {
            templateUrl: './settings/start.html?=' + version,
            controller: 'IndexController'
        }).when('/algo/:ivalueId?', {
            templateUrl: './vacancy/algo.html?=' + version,
            controller: 'AlgoViewController'
        }).when('/task/:id?', {
            templateUrl: './vacancy/task.html?=' + version,
            controller: 'TaskViewController'
        }).when('/owner', {
            templateUrl: './manage/vacancies.html?=' + version,
            controller: 'VacanciesOwnerController'
        }).when('/invited/cv/:key', {
            templateUrl: './manage/invited_by_cv.html?=' + version,
            controller: 'InvitedByCvController'
        }).when('/taken', {
            templateUrl: './vacancy/taken.html?=' + version,
            controller: 'InterviewTakenController'
        }).when('/vacancy/update/:id?', {
            templateUrl: './manage/update.html?=' + version,
            controller: 'VacancyUpdateController'
        }).when('/vacancy/copy/:id?', {
            templateUrl: './manage/copy.html?=' + version,
            controller: 'VacancyCopyController'
        }).when('/team/update/:id?', {
            templateUrl: './manage/team_update.html?=' + version,
            controller: 'TeamUpdateController'
        }).when('/teams', {
            templateUrl: './manage/teams.html?=' + version,
            controller: 'TeamsController'
        }).when('/task/update/:vacancyId/:id?', {
            templateUrl: './manage/task_update.html?=' + version,
            controller: 'TaskUpdateController'
        }).when('/interview/:id', {
            templateUrl: './vacancy/vacancy.html?=' + version,
            controller: 'InterviewViewController'
        }).when('/vacancy/:id', {
            templateUrl: './manage/vacancy.html?=' + version,
            controller: 'VacancyViewController'
        }).when('/vacancy/invited/:vacancyId?', {
            templateUrl: './manage/invited.html?=' + version,
            controller: 'SendEmailController'
        }).when('/vacancy/:vacancyId/report/update/:id?', {
            templateUrl: './manage/report_update.html?=' + version,
            controller: 'ReportUpdateController'
        }).when('/next/:interviewId', {
            templateUrl: './vacancy/interview.html?=' + version,
            controller: 'InterviewNextController'
        }).when('/vacancy/candidates/:vacancyId/:result/:interviewId?', {
            templateUrl: './manage/candidates.html?=' + version,
            controller: 'CandidatesController'
        }).when('/vacancy/details/:interviewId/:key', {
            templateUrl: './manage/details_interview.html?=' + version,
            controller: 'DetailsInterviewController'
        }).when('/archive', {
            templateUrl: './manage/archive.html?=' + version,
            controller: 'ArchiveController'
        }).otherwise({
            templateUrl: './empty.html?=' + version,
            controller: 'RouteController'
        });
    }]);