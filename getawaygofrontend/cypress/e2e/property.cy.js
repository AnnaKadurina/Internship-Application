describe('Properties', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000');
        cy.wait(5000)
        cy.get('#address').clear();
    });

    it('display properties', () => {
        cy.get('#propertyCard').should('be.visible');
        cy.get('#address').should('be.visible');
        cy.get('#price').should('be.visible');
    });

    it('get property information and reviews', () => {
        cy.get('#propertyCard').should('be.visible');
        cy.wait(5000);
        cy.get('#propertyCard').get('#showMore').click();
        cy.wait(5000);
        cy.get('#propertyInfo').should('be.visible');
        cy.wait(5000);
        cy.get('#reviews').should('be.visible');
        cy.wait(5000)
        cy.get('#reviews').should('contain', 'Average rating:');
        cy.get('#reviews').should('contain', 'Total reviews:');
    });

    it('filter properties', () => {
        cy.get('#propertyCard').should('be.visible');
        cy.wait(5000)
        cy.get('#address').type('Blagoevgrad');
        cy.wait(5000)
        cy.get('.pac-container')
            .within(() => {
                cy.get('.pac-item')
                    .first()
                    .click();
            })

        cy.wait(5000);
        cy.get('#showListings').click();

        cy.wait(10000);
        cy.get('#propertyCard').should('be.visible');
    });

});
