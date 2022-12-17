# Library
 Lib工程，抽象封装一些机制，提高adapter编码效率及可读性

# AbsStyleAdapter
 自己组装数据，实现数据驱动列表UI思想；
 * 支持自定义任意type及style的多类型布局，且UI可通过数据实现快速配置；
 * 适用任意可滑动页面场景（不用非要是常规列表）；
 * 页面需提交资料时，一定要从列表的数据中取值判断，而不通过列表Item UI控件取资料数据，
   可极大降低数据信息的逻辑获取能力。
   
# AbsChoiceAdapter
 支持多选模式，常规单类型Item列表