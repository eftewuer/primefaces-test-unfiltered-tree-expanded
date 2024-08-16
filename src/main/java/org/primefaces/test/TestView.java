package org.primefaces.test;

import lombok.Data;
import org.primefaces.PrimeFaces;
import org.primefaces.component.api.UITree;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.util.LangUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Data
@Named
@ViewScoped
public class TestView implements Serializable {
    private TreeNode<String> root;
    private UITree tree;
    private String filterText = "";

    public TreeNode<String> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<String> root) {
        this.root = root;
    }

    public UITree getTree() {
        return tree;
    }

    public void setTree(UITree tree) {
        this.tree = tree;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode<>("Files", null);

        TreeNode<String> node0 = new DefaultTreeNode<>("Documents", root);
        TreeNode<String> node1 = new DefaultTreeNode<>("Events", root);
        TreeNode<String> node2 = new DefaultTreeNode<>("Movies", root);

        TreeNode<String> node00 = new DefaultTreeNode<>("Work", node0);
        TreeNode<String> node01 = new DefaultTreeNode<>("Home", node0);
        node0.getChildren().add(new DefaultTreeNode<>("Tom"));
        node0.getChildren().add(new DefaultTreeNode<>("John"));

        node00.getChildren().add(new DefaultTreeNode<>("Expenses.doc"));
        node00.getChildren().add(new DefaultTreeNode<>("Resume.doc"));
        node01.getChildren().add(new DefaultTreeNode<>("Invoices.txt"));

        node1.getChildren().add(new DefaultTreeNode<>("Meeting"));
        node1.getChildren().add(new DefaultTreeNode<>("Product Launch"));
        node1.getChildren().add(new DefaultTreeNode<>("Report Review"));

        TreeNode<String> node20 = new DefaultTreeNode<>("Al Pacino", node2);
        TreeNode<String> node21 = new DefaultTreeNode<>("Robert De Niro", node2);

        node20.getChildren().add(new DefaultTreeNode<>("Scarface"));
        node20.getChildren().add(new DefaultTreeNode<>("Serpico"));

        node21.getChildren().add(new DefaultTreeNode<>("Goodfellas"));
        node21.getChildren().add(new DefaultTreeNode<>("Untouchables"));

        node21.getChildren().add(new DefaultTreeNode<>("Tom"));
        node21.getChildren().add(new DefaultTreeNode<>("John"));
    }

    public boolean customFilter(TreeNode<String> treeNode, Object filter, Locale locale) {
        if (treeNode.getData() == null || filter == null) {
            return true;
        }
        String filterText = filter.toString().trim().toLowerCase(locale);
        if (LangUtils.isBlank(filterText)) {
            return true;
        }

        return  treeNode.getData().toLowerCase(locale).contains(filterText);
    }

    public void onFilter(AjaxBehaviorEvent e) {
        FacesContext context = e.getFacesContext();
        String clientId = e.getComponent().getClientId();

        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String filteredValue = params.get(clientId + "_filter");

        filterText = filteredValue;
    }

    public void filterComplete() {
        if(!(filterText != null && !filterText.isEmpty())) {
            List<TreeNode<String>> children = root.getChildren();
            children.forEach(child -> {
                child.setExpanded(false);
            });
            //uncomment below to force to refresh tree - makes the tree blink
            //but can be a workaround
            //PrimeFaces.current().executeScript("updateTreeCommand()");
        }

    }

}
