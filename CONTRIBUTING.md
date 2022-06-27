### 提交错误报告

如果您在XXX中发现了一个不存在安全问题的漏洞, 请在XXX仓库中的 Issues 中搜索, 以防该漏洞已被提交, 如果找不到漏洞可以创建一个新的 Issues.
如果发现了一个安全问题请不要将其公开. 请参阅安全问题处理方式. 提交错误报告时应该详尽

### 安全问题处理

本项目中对安全问题处理的形式. 项目核心人员确认编辑. 该部分内容可以根据项目情况添加

### 解决现有问题

通过查看仓库的 Issues 列表何以发现需要处理的问题信息, 可以尝试解决其中的某个问题

### 如何提出新功能

提出新功能有些项目使用 Issues 的 Feature 标签进行管理, 有些则通过邮件的形式统一收集. 在收集后项目内人员会进行确认开发, 一般的将确认开发的功能会放入下一个版本的任务列表

### 如何设置开发环境并运行测试

如果是通过 Git 管理可以从 `git clone xxx` 开始编写, 将开发环境的配置信息, IDE 的设置等信息配置文档编写.

### 变更日志填写规则

1. 使用现在时态
1. 第一行字数限制
1. 提交内容的约束

### 编码约定

- 项目内编码约定文件, .editorconfig、code_checks.xml、code_style.xml
- code_checks.xml使用：1：idea安装CheckStyle插件，2：导入code_checks.xml文件
- code_style.xml使用：1：打开idea->settings->Editor->Code Style->scheme->import scheme
  2：导入code_style.xml文件
- 安装Gitmoji Plus: Commit Button插件，在commit的时候可以选择相应的图标显示在commit message前方，可以快速看出当前提交内容的类型，也可以为编码增添点乐趣
  ❤️

File Header模板：打开File->Settings->Editor->File and Code Templates->Includes->File Header设置

```java
/**
 * @author 真实姓名
 * @since ${YEAR}-${MONTH}-${DAY}
 */
```

> 为了项目代码整洁度和以后code review的质量，请各位开发人员遵守以上规范 ❤️

### 分支处理约定

- 分支处理形式, 如 gitFlow

### 合并 PR 的形式

在什么情况下可以合并到 master/main.

1. 通过 CI
2. 两个及以上的维护者通过.
3. 最新版本

