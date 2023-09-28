describe('Booking', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000/login');
        cy.wait(5000)
        cy.get('#username').type('AniGuest');
        cy.get('#password').type('test1234');
        cy.get('form').submit();
        cy.url().should('include', '/home');
    });


    it('Get booking form', () => {
        cy.get('#propertyCard').should('be.visible');
        cy.wait(5000);
        cy.get('#propertyCard').get('#showMore').click();
        cy.wait(5000);
        cy.get('#book').should('be.visible');
        cy.wait(5000);
        cy.get('#book').click();
        cy.wait(5000);
        cy.get('#createUpdateForm').should('be.visible');
        cy.wait(5000)

    });

    it('Get all bookings for GUEST', () => {
        cy.get('#profile').click();
        cy.contains('My bookings').click();
        cy.wait(5000);
        cy.get('#bookingInfo').should('be.visible')

    });
});

describe('Planning', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000/login');
        cy.wait(5000)
        cy.get('#username').type('AniHost');
        cy.get('#password').type('test1234');
        cy.get('form').submit();
        cy.url().should('include', '/home');
    });


    it('See planning and open chat', () => {
        cy.get('#profile').click();
        cy.contains('Manage my properties').click();
        cy.get('body').click();
        cy.wait(5000);
        cy.get('#properties').should('be.visible');
        cy.get('#planning').click();
        cy.wait(5000);
        cy.get('#calendarPlanning').should('be.visible');
        cy.get('#bookingsOfProperty').should('be.visible');
        cy.get('#chat').click();
        cy.wait(5000);
        cy.get('.chat-box').should('be.visible');

    });
});