describe('Login', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000/login');
    });


    it(' login invalid credentials', () => {
        cy.get('#username').type('AniGuest');
        cy.get('#password').type('1234');
        cy.get('form').submit();
        cy.contains('INVALID_CREDENTIALS').should('be.visible');
    });

    it(' login valid, role Admin', () => {
        cy.get('#username').type('AniAdmin');
        cy.get('#password').type('test1234');
        cy.get('form').submit();
        cy.url().should('include', '/home')

        cy.get('#profile').click();
        cy.contains('Settings').should('be.visible');
        cy.contains('Users').should('be.visible');
        cy.contains('Properties').should('be.visible');
        cy.contains('Reviews').should('be.visible');
        cy.contains('Statistics').should('be.visible');
        cy.contains('Logout').should('be.visible');

        cy.wait(10000);


    });

    it(' login valid, role Host', () => {
        cy.get('#username').type('AniHost');
        cy.get('#password').type('test1234');
        cy.get('form').submit();
        cy.url().should('include', '/home')

        cy.get('#profile').click();

        cy.contains('Settings').should('be.visible');
        cy.contains('Add new listing').should('be.visible');
        cy.contains('Manage my properties').should('be.visible');
        cy.contains('My chats').should('be.visible');
        cy.contains('Statistics').should('be.visible');
        cy.contains('Logout').should('be.visible');
    });

    it(' login valid, role Guest', () => {
        cy.get('#username').type('AniGuest');
        cy.get('#password').type('test1234');
        cy.get('form').submit();
        cy.url().should('include', '/home')

        cy.get('#profile').click();

        cy.contains('Settings').should('be.visible');
        cy.contains('My bookings').should('be.visible');
        cy.contains('My chats').should('be.visible');
        cy.contains('Logout').should('be.visible');
    });
});

describe('Register', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000/register');
    });


    it(' register weak password ', () => {
        cy.get('#firstName').type('Anna');
        cy.get('#lastName').type('Kadurina');
        cy.get('#username').type('Aniii');
        cy.get('#email').type('Ani@gmail.com');
        cy.get('#phone').type('852596');
        cy.get('#address').type('Blg');
        cy.get('#password').type('1234');
        cy.get('form').submit();
        cy.contains('WEAK_PASSWORD').should('be.visible');
    });

    it(' register email invalid ', () => {
        cy.get('#firstName').type('Anna');
        cy.get('#lastName').type('Kadurina');
        cy.get('#username').type('Aniii');
        cy.get('#email').type('Ani');
        cy.get('#phone').type('852596');
        cy.get('#address').type('Blg');
        cy.get('#password').type('Test1234()');
        cy.get('form').submit();
        cy.contains('EMAIL_FORMAT_NOT_CORRECT').should('be.visible');
    });
});