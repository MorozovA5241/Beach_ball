package io.github.beachball.managers;

import static io.github.beachball.GameSettings.FLOOR_BIT;
import static io.github.beachball.GameSettings.PLAYER_BIT;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import io.github.beachball.GameObject;

public class ContactManager {
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

                if(cDefA == PLAYER_BIT && cDefB == FLOOR_BIT){
                    ((GameObject) fixA.getUserData()).setJumps(0);
                }

                if(cDefB == PLAYER_BIT && cDefA == FLOOR_BIT){
                    ((GameObject) fixB.getUserData()).setJumps(0);
                }


            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }
}
