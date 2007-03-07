package org.exoplatform.services.xml.querying.impl.xtas;

import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.xml.sax.InputSource;
import java.io.SequenceInputStream;
import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Helper to UniFormTree subclasses transformation 
 */
public class UniFormConverter {

    /**
     * Converts UniFormTreeFragment to WellFormedUniFormTree
     */
    public static WellFormedUniFormTree toWellForm(UniFormTree tree) throws UniFormTransformationException
    {

        if (tree instanceof WellFormedUniFormTree)
            return (WellFormedUniFormTree)tree;
        else if  (tree instanceof UniFormTreeFragment) {

            WellFormedUniFormTree wfTree = new WellFormedUniFormTree();
            wfTree.init( new InputSource(tree.getAsInputStream()) );
            return wfTree;

        }
        else
            throw new UniFormTransformationException( "The type "+tree.getClass().getName()+
                      " is not transformable to WellFormedUniFormTree! ");
    }

    /**
     * Converts UniFormTree to WellFormedUniFormTree
     * and inserts Element named rootName as root 
     */
    public static WellFormedUniFormTree toWellForm(UniFormTree tree, String rootName) throws UniFormTransformationException
    {

        if ( (tree instanceof WellFormedUniFormTree) || (tree instanceof UniFormTreeFragment) ) {

            WellFormedUniFormTree wfTree = new WellFormedUniFormTree();
            SequenceInputStream s = new SequenceInputStream(
                 new ByteArrayInputStream( ("<"+rootName+">").getBytes() ), tree.getAsInputStream() );
            s = new SequenceInputStream( 
                 s, new ByteArrayInputStream( ("</"+rootName+">").getBytes() ) );

            wfTree.init( new InputSource(s) );
            return wfTree;
        }
        else
            throw new UniFormTransformationException( "The type "+tree.getClass().getName()+
                      " is not transformable to WellFormedUniFormTree! ");

    }

    /**
     * Converts UniFormTree to WellFormedUniFormTree
     * and inserts root Element  
     */
    public static WellFormedUniFormTree toWellForm(UniFormTree tree, String rootName, String namespaceURI, Properties attrs) throws UniFormTransformationException
    {
        if ( (tree instanceof WellFormedUniFormTree) || (tree instanceof UniFormTreeFragment) ) {

            WellFormedUniFormTree wfTree = new WellFormedUniFormTree();
            String attributes = "";
            for (Enumeration e = attrs.propertyNames(); e.hasMoreElements() ;) {
               String name = (String)e.nextElement();
               attributes+=name+"=\""+attrs.getProperty(name)+"\" ";
            }
            String str = "<"+rootName+" "+namespaceURI+" "+attributes+">";
            SequenceInputStream s = new SequenceInputStream(
                 new ByteArrayInputStream( str.getBytes() ), tree.getAsInputStream() );
            s = new SequenceInputStream( 
                 s, new ByteArrayInputStream( ("</"+rootName+">").getBytes() ) );

            wfTree.init( new InputSource(s) );
            return wfTree;
        }
        else
            throw new UniFormTransformationException( "The type "+tree.getClass().getName()+
                      " is not transformable to WellFormedUniFormTree! ");

    }

    /**
     * Converts WellformedUniFormTree to UniFormTreeFragment
     */
    public static UniFormTreeFragment toFragment(UniFormTree tree) throws UniFormTransformationException
    {
        if (tree instanceof UniFormTreeFragment)
            return (UniFormTreeFragment)tree;
        else if  (tree instanceof WellFormedUniFormTree) {

            UniFormTreeFragment fragment = new UniFormTreeFragment();
            fragment.init( tree.getAsInputStream() );
            return fragment;

        }
        else
            throw new UniFormTransformationException( "The type "+tree.getClass().getName()+
                      " is not transformable to UniFormTreeFragment! ");

    }
}
