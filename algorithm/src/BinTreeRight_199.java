import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 199.二叉树的右视图
 * <p>
 * description
 * <p>
 * 给定一棵二叉树，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值
 */
public class BinTreeRight_199 {

    public List<Integer> rightSideView(TreeNode root) {
        /**
         * way 层序遍历取每一层最后一位
         */
        if (root == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> nodes = new LinkedList<>();
        nodes.add(root);
        while (nodes.size() > 0) {
            int size = nodes.size();
            for (int i = 0; i < size; i++) {
                TreeNode poll = nodes.poll();
                if (poll.left != null) {
                    nodes.offer(poll.left);
                }
                if (poll.right != null) {
                    nodes.offer(poll.right);
                }
                if (i == size - 1) {
                    result.add(poll.val);
                }
            }

        }
        return result;
    }

    public List<Integer> rightSideView2(TreeNode root) {
        /**
         * way dfs
         */
        List<Integer> result = new ArrayList<>();
        getByDFS(root,0,result);
        return result;
    }

    public void getByDFS(TreeNode node, int depth,List<Integer> result){
        if (node==null){
            return;
        }
        if (depth ==result.size()){
            result.add(node.val);
        }
        depth++;
        getByDFS(node.right,depth,result);
        getByDFS(node.left,depth,result);
    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        TreeNode treeNode1 = new TreeNode(2);
        treeNode1.right = new TreeNode(5);
        TreeNode treeNode2 = new TreeNode(3);
        treeNode1.right = new TreeNode(4);
        treeNode.left = treeNode1;
        treeNode.right = treeNode2;
        BinTreeRight_199 binTreeRight_199 = new BinTreeRight_199();
        System.out.println(binTreeRight_199.rightSideView(treeNode));
        System.out.println(binTreeRight_199.rightSideView2(treeNode));
    }


    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}


