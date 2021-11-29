import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 106.根据一棵树的中序遍历与后序遍历构造二叉树。
 */

public class RebuildBinTreeByOrder_106 {
    /**
     * 递归
     * 1.确定后序遍历中 [左、右、根]和中序遍历的[左、根、右]的关系
     * 2.使用一个 map 存入中序遍历，用于在中序遍历中查找值所在位置时的 O1 复杂度
     */
    int post_idx;
    private int[] inorder;
    private int[] postorder;
    private Map<Integer, Integer> map;
    public List<Integer> buildTree(int[] inorder, int[] postorder) {
        this.inorder = inorder;
        this.postorder = postorder;
        this.map = new HashMap<>();
        this.post_idx = postorder.length-1;
        for (int i = 0; i < postorder.length; i++) {
            map.put(inorder[i], i);
        }
        TreeNode rebuild = rebuild(0, inorder.length - 1);
        List<Integer> a = new ArrayList<>();
        inorder(rebuild,a);
        return a;
    }

    public TreeNode rebuild(int left, int right) {
        if (left > right) {
            return null;
        }
        int rootVal = postorder[post_idx];
        TreeNode treeNode = new TreeNode(rootVal);
        Integer inRootIndex = map.get(rootVal);
        post_idx--;
        treeNode.right = rebuild( inRootIndex+1, right);
        treeNode.left = rebuild(left, inRootIndex-1);
        return treeNode;
    }

    /**
     * 前序遍历
     * @param node
     * @param a
     */
    public void inorder(TreeNode node,List<Integer> a){
        if (node ==null){
            return;
        }
        a.add(node.val);
        inorder(node.left,a);
        inorder(node.right,a);
    }

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

    public static void main(String[] args) {
        int[] a = new int[]{9, 3, 15, 20, 7};
        int[] b = new int[]{9, 15, 7, 20, 3};
        RebuildBinTreeByOrder_106 solution = new RebuildBinTreeByOrder_106();
        System.out.println(solution.buildTree(a, b));
    }

}
