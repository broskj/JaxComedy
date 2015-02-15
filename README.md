# JaxComedy
Android app for the Comedy Club of Jacksonville.  Used to get access to information about the Club and our comics and to reserve seats ahead of time.  Also gives exclusive access to a rewards program with exclusive mobile-only deals.

Structure:
    -MainActivity -> {This Weekend, Upcoming Shows, Rewards and Offers, Food and Drink, Groups and Parties;
      Reservation, Add Reward Points, Redeem Offer}
        -This Weekend -> Reservation
            -Current information for the weekend show.  Supply information from a spreadsheet, and update
              regularly if possible.  This information will be the content of the weekly notifications, and
              the notifications will link to this page.  A video widget at the top will show a youtube video of
              the comedian.
                -Able to reserve seats from here; include a reserve button for the next, and pass that 
                  information to the reservation page.
        -UpcomingShows -> Reservation
            -Displays ordered list of dates which there are shows, and include information such as show time, 
              comedian, etc.
                -Able to reserve seats from here; expandable list view children navigate to the reservation page, 
                  and pass that information to the reservation page.
        -Rewards and Offers -> {Add Reward Points, Redeem Offer}
            -Populate from google spreadsheet.  Each deal will contain a title, description, and point value.
              Deals come from a Rewards Program, where points are entered each ticket purchase and confirmed 
              with a 4-digit pass code at the door.  Redeeming a reward resets/decrements the total point value a
              certain amount.  Error message is displayed when the point value isn't high enough.  Initial point
              value set high enough for automatic access to some deal.  Include some deals with 0 point values
              that are unclickable.
        -Food and Drink
            -Expandable list of item names and prices, with expanded text being description of item.  Mostly
              used for food items and drink specials.
        -Groups and Parties -> {Contact, Upcoming Shows}
            -Includes group/party overview with contact information for additional offers.  No reservations from
              this page, but contains buttons to 'Contact' and 'Upcoming Shows'.
        -Reservation
            -Activity used to select showtime/date and guest number, and to leave a reservation name.  Needs to perform
               checks on fields like name, email, and guests (name has to be included, guests cannot be over 20) and needs to
               enable/disable dates and show times based on availability (cannot reserve seats for a sold out show, a
               show time that doesn't exist, etc.).
            -Sends email confirmation to the user (confirmation code based on random number generator?) and to
              info@jacksonvillecomedy.com, maybe to an alternate email for me as well for easier record keeping.
        -Add Reward Points
            -Reached from Rewards and Offers by clicking point value area near top.
            -Includes fields for number of tickets purchased and for a 4-digit pass code to be entered at the door.
              A confirm button checks for a correct password and if it's correctly entered, point value increments
              and saves to device.
        -Redeem Offer
            -Confirmation page.  Requires password, and pushing confirm with correct password decrements point values.
              Either needs to be presented at door or at table; deal needs to be confirmed and the customer needs
              to be given a physical coupon if presented at the door, and if presented to a waiter/waitress, deal needs
              to be confirmed and administered immediately upon confirmation (i.e., free appetizer coupon gets confirmed
              and sent through the POS during the same interaction with the customer).
