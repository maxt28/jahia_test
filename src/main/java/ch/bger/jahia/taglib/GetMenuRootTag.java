package ch.bger.jahia.taglib;

import javax.jcr.RepositoryException;
import javax.servlet.jsp.JspException;

import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.taglibs.AbstractJahiaTag;
import org.slf4j.Logger;

/**
 * Get the root under the homepage for a page.
 * 
 * Example :
 * Home
 * - Menu Root 1
 *   - Page 11
 *   - Page 12
 *     - Page 121
 * - Menu Root 2
 *   -Page 21
 *    - Page 211
 * 
 * For node "Page 121", the result is "Menu Root 1"
 * For node "Page 11", the result is "Menu Root 1"
 * For node "Page 21", the result is "Menu Root 2"
 * 
 * @author Marc Maury <mrmau@smile.fr>
 */
public class GetMenuRootTag extends AbstractJahiaTag {

    /** comm*/
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GetMenuRootTag.class);
    /** com*/
    private String var;
    /** com*/
    private JCRNodeWrapper pageNode;

    /** com*/
    public JCRNodeWrapper getRootNode(final JCRNodeWrapper node) {
        try {
            final String homepagePath = getRenderContext().getSite().getHome().getPath();
            JCRNodeWrapper currentNode = pageNode;
            JCRNodeWrapper parentNode = pageNode.getParent();
            while (parentNode != null && !homepagePath.equals(parentNode.getPath())) {
                currentNode = currentNode.getParent();
                parentNode = currentNode.getParent();
            }
            return currentNode;
        } catch (RepositoryException e) {
            LOGGER.error("Fail to get homepage node.", e);
        }
        return null;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            final String homepagePath = getRenderContext().getSite().getHome().getPath();
            JCRNodeWrapper currentNode = pageNode;
            JCRNodeWrapper parentNode = pageNode.getParent();
            while (parentNode != null && !homepagePath.equals(parentNode.getPath())) {
                currentNode = currentNode.getParent();
                parentNode = currentNode.getParent();
            }
            pageContext.setAttribute(var, currentNode);
        } catch (RepositoryException e) {
            LOGGER.error("Fail to get homepage node.", e);
        }

        return EVAL_BODY_INCLUDE;
    }

    public String getVar() {
        return var;
    }

    public void setVar(final String var) {
        this.var = var;
    }

    public JCRNodeWrapper getPageNode() {
        return pageNode;
    }

    public void setPageNode(final JCRNodeWrapper pageNode) {
        this.pageNode = pageNode;
    }
}
