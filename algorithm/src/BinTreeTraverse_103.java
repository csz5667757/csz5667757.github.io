import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 103. 二叉树的锯齿形层序遍历
 * <p>
 * 给定一个二叉树，返回其节点值的锯齿形层序遍历。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
 */

public class BinTreeTraverse_103 {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {

        /**
         * way 广度优先遍历
         */
        List<List<Integer>> result = new LinkedList<>();
        if (root == null) {
            return result;
        }
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        nodeQueue.offer(root);
        boolean isOrderLeft = true;

        while (!nodeQueue.isEmpty()){
            Deque<Integer> levelList = new LinkedList<>();
            int size = nodeQueue.size();
            for (int i = 0; i < size; i++) {
                TreeNode current = nodeQueue.poll();//删除队首并返回
                if (isOrderLeft){
                    levelList.offerLast(current.val);
                }else {
                    levelList.offerFirst(current.val);
                }
                if (current.left!=null){
                    nodeQueue.offer(current.left);
                }
                if (current.right!=null){
                    nodeQueue.offer(current.right);
                }
            }
            isOrderLeft =!isOrderLeft;
            result.add(new LinkedList<>(levelList));
        }
        return result;
    }


    /**
     * Definition for a binary tree node.
     */
    public class TreeNode {
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
