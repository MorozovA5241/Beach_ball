package io.github.beachball.managers;

import static io.github.beachball.GameSettings.BAFFLE_BIT;
import static io.github.beachball.GameSettings.BALL_BIT;
import static io.github.beachball.GameSettings.FLOOR_BIT;
import static io.github.beachball.GameSettings.PLAYER_BIT;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import io.github.beachball.GameObject;

public class ContactManager { // этот прикол отслеживает контакты в физике
    World world;

    public ContactManager(World world){
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDefA = fixA.getFilterData().categoryBits;
                int cDefB = fixB.getFilterData().categoryBits;

                if(cDefA == PLAYER_BIT && cDefB == FLOOR_BIT){ // проверяем все случаи ( fixA и fixB могут меняться местами)
                    ((GameObject) fixA.getUserData()).setJumps(0);
                }

                if(cDefB == PLAYER_BIT && cDefA == FLOOR_BIT){
                    ((GameObject) fixB.getUserData()).setJumps(0);
                }

                if(cDefA == BALL_BIT && cDefB == FLOOR_BIT){
                    ((GameObject) fixA.getUserData()).setPosition();
                }

                if(cDefB == BALL_BIT && cDefA == FLOOR_BIT){
                    ((GameObject) fixB.getUserData()).setPosition();

                }

                if(cDefA == BALL_BIT && cDefB == PLAYER_BIT){
                    ((GameObject) fixA.getUserData()).setScore();
                }

                if(cDefB == BALL_BIT && cDefA == PLAYER_BIT){
                    ((GameObject) fixB.getUserData()).setScore();

                }





            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDefA = fixA.getFilterData().categoryBits;
                int cDefB = fixB.getFilterData().categoryBits;
                if(cDefA == BALL_BIT && cDefB == BAFFLE_BIT || cDefB == BALL_BIT && cDefA == BAFFLE_BIT){
                    contact.setEnabled(false);
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {


            }
        });
    }
}
