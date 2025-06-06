# 项目文档

![星开祈灵百宝箱 项目文档-1.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-1.png?v=1)



## 📖 项目简介

> 《星开祈灵百宝箱》
>
> 一个整合多个小工具或小游戏的开源项目集合
>
> 包含批量图片水印处理、简单爬虫脚本、连连看游戏等模块，采用 SpringBoot + Vue3 技术栈实现



## 🚀 快速开始

### ⚙️ 环境要求

|  环境   |  版本   |   备注   |
| :-----: | :-----: | :------: |
|   JDK   |   17    | 严格匹配 |
| Node.js | 20.16.0 | LTS版本  |
|  MySQL  |   5.7   | 最低要求 |
|  Redis  |  5.0+   | 兼容版本 |

❗ 版本警告：

   本项目使用 **精确版本锁定** 策略，若使用更高版本组件可能导致兼容性问题 

   （测试通过版本见上文，建议使用相同版本避免环境问题）

### 🔧 启动步骤

1. **克隆项目**

   ```bash
   git clone https://github.com/KokoaChino/xkql.git
   ```

2. **数据库初始化**

   数据库初始化脚本位于 `/sql/xkql.sql`

   ```bash
   # 第一步：创建数据库
   mysql -u root -p -e "CREATE DATABASE xkql CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
   
   # 第二步：导入表结构及数据（注意文件路径）
   mysql -u root -p xkql < ./sql/xkql.sql
   ```

   或通过Navicat可视化操作：

   1. 新建连接 -> 输入root/123456
   2. 右键连接 -> 创建数据库：
      - 数据库名：`xkql`
      - 字符集：`utf8mb4`
      - 排序规则：`utf8mb4_general_ci`
   3. 双击打开新数据库 -> 右键数据库 -> 运行SQL文件：
      - 文件选择项目中的 `/sql/xkql.sql`
      - 编码选择 `UTF-8`

3. **启动 Redis 服务**

   ```bash
   redis-server # 确保 Redis 已安装，并运行在默认端口 6379
   ```

4. **后端启动**

   ```bash
   cd backend
   mvn spring-boot:run # 确保已配置数据库连接（application.yml）
   ```

5. **前端启动**

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

   访问 `http://localhost:5174` 即可



## 📌 使用说明

在线访问地址：http://8.138.214.176:5174

本地访问地址：http://localhost:5174

### ➤ 账号体系说明

#### 1. 注册账号

点击前往**注册账号**

![星开祈灵百宝箱 项目文档-2.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-2.png?v=1)

**填写用户名，密码等相关信息**后，再点击**获取验证码**

大概半分钟后，系统就会发送验证码至你的邮箱

验证码三分钟之内有效，一分钟内不得重复获取验证码

![星开祈灵百宝箱 项目文档-3.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-3.png?v=1)

#### 2. 登录界面

填写**用户名 / 邮箱 + 密码**进行登录

可以勾选**记住我**功能（用户在登录后的一段时间内，只要不退出登录，则不需要验证，就可以直接登录）

![星开祈灵百宝箱 项目文档-4.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-4.png?v=1)

#### 3. 忘记密码

点击前往**忘记密码**模块

![星开祈灵百宝箱 项目文档-5.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-5.png?v=1)

首先需要**进行电子邮件的验证**，依然是通过验证码的形式

![星开祈灵百宝箱 项目文档-6.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-6.png?v=1)

之后**填写新密码**即可

![星开祈灵百宝箱 项目文档-7.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-7.png?v=1)

#### 4. 退出登录

主页，点击右上角的绿色按钮

![星开祈灵百宝箱 项目文档-8.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-8.png?v=1)

#### 5. 注销账号

主页，点击左上角的红色按钮

![星开祈灵百宝箱 项目文档-9.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-9.png?v=1)

会出来一个确认窗口，点击确认即可

![星开祈灵百宝箱 项目文档-10.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-10.png?v=1)

### ➤ 各模块特殊说明

#### 1. 猜数字游戏

![星开祈灵百宝箱 项目文档-11.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-11.png?v=1)

太简单了没啥好说的

![星开祈灵百宝箱 项目文档-12.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-12.png?v=1)

这里记录着你的最佳成绩

如果没有记录，则显示为 NULL

#### 2. 角色自截图

##### 功能入口

当鼠标悬停在角色图片区域时，将触发交互式信息面板显示：

![星开祈灵百宝箱 项目文档-13.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-13.png?v=1)

*界面要素说明：*

- 自动弹出角色简介卡片
- 显示两个功能按钮：【查看图片】和【下载图片】

##### 功能操作流程

1. **查看完整图集**

点击【查看图片】按钮后，系统将跳转至专用展示页面：

![星开祈灵百宝箱 项目文档-14.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-14.png?v=1)

*页面特性：*

- 提供单张下载选项
- 包含图片元数据展示

2. **批量下载操作**

在悬停面板直接点击【下载图片】按钮：

![星开祈灵百宝箱 项目文档-15.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-15.png?v=1)

*系统响应：*

- 自动下载角色专属压缩包（ZIP格式）
- 保持原始图片分辨率

##### 下载文件处理

成功下载的压缩包解压后效果：

![星开祈灵百宝箱 项目文档-16.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-16.png?v=1)


#### 3. 连连看游戏

##### 游戏启动
 点击【开始游戏】后，系统将自动从游戏库中随机抽取n×n方阵

（注：当前版本暂未开放9×9至13×13尺寸方阵，问就是设计游戏图太烧脑了）

![星开祈灵百宝箱 项目文档-17.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-17.png?v=1)

##### 核心规则
1. **矩阵结构**：采用n×n的矩阵布局，包含若干对同色图块
2. **连接要求**：
   - 必须通过连续路径连接同色图块对
   - 路径禁止发生交叉或重叠
3. **胜利条件**：当所有图块成功完成合法配对时，游戏自动结束

##### 操作指南
1. **路径绘制**：
   - 按住鼠标左键选定起点图块/路径端点
   - 保持按压状态拖动至目标位置
   - 松开左键完成路径连接

2. **路径编辑**：
   - 双击已绘制路径 → 删除该连接
   - 双击已连接图块 → 删除其关联路径
   - （编辑模式专有）双击未连接图块 → 删除该图块

![星开祈灵百宝箱 项目文档-18.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-18.png?v=1)

##### 游戏库管理（管理员权限）
- **功能入口**：第0页为专用编辑工作区
- **新建方阵**：
  1. 在编辑页设计合规矩阵
  2. 点击【提交】存入游戏库
- **修改现有**：
  1. 选择目标方阵所在页码
  2. 完成修改后必须执行【提交】操作
  3. 变更内容即时同步至游戏库

#### 4. 简单爬虫脚本

![星开祈灵百宝箱 项目文档-19.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-19.png?v=1)

##### 实现原理
基于HTTP协议模拟浏览器请求，通过深度优先搜索算法遍历目标网站的所有可访问链接

当检测到重定向响应时，系统会自动跟踪跳转地址并递归执行抓取任务

##### 核心参数说明
- **递归深度控制**
  取值范围：1-10 层
  
- **资源链接过滤**
  启用"解析下载链接"选项后，只解析那些疑似可以下载的资源链接

![星开祈灵百宝箱 项目文档-20.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-20.png?v=1)

##### 操作流程
1. **启动爬取** 
   点击【开始解析】后，任务队列将显示实时抓取进度

2. **运行控制**  
   - 点击【停止解析】立即停止当前任务
   - 页面关闭或无网络响应时自动终止进程

3. **结果交互** 
   成功解析的链接将支持单击直接在新标签页打开


#### 5. 批量图片水印处理

##### 主操作界面
![星开祈灵百宝箱 项目文档-21.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-21.png?v=2)

界面布局包含以下功能区域（自上而下、从左至右排列）：
1. **压缩文件上传区** - 支持ZIP格式文件点击上传
2. **任务进度监控条** - 实时显示批量处理进度百分比
3. **任务结果反馈栏** - 处理完成后显示统计报表
4. **水印样式库** - 预设样式及自定义样式存储区
5. **样式切换面板** - 支持不同水印模板的即时切换
6. **参数配置面板** - 调整水印位置/阴影/字体等属性
7. **实时预览窗口** - 可视化展示当前水印效果

##### 文件规范要求
![星开祈灵百宝箱 项目文档-22.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-22.png?v=1)

![星开祈灵百宝箱 项目文档-23.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-23.png?v=1)

压缩包文件需满足以下技术规范：
1. 文件格式限定为ZIP标准压缩格式
2. 压缩包内容要求：
   - 仅允许包含以下两种文件类型：
     * JPEG格式图片（.jpg/.jpeg）
     * Excel数据表（.xlsx）
   - 禁止包含其他类型文件或嵌套压缩包

3. 图片处理规则配置说明

   您的图片将根据以下表格规则自动处理（配置区域位于Excel的第一个工作表的A列开始，跳过首行）

   **各列配置说明**：

   * A列（必填）图片唯一ID → 需与图片文件名图片文件名完全匹配
   * B列（选填）水印文字 → 留空则等于啥也没做
   * C列（选填）自定义文件名 → 留空时将自动使用A列的图片ID
   * D-$\infty$列（选填）分级目录 → 从D列开始向右连续填写目录层级

   **路径生成规则**：

   1. 目录结构：系统将自动串联D列及右侧连续的非空单元格作为路径 （示例：D2=2023，E2=Q3 → 生成路径 /2023/Q3/）
   2. 最终路径 = 目录路径 + "/" + [C列内容或A列ID] + 扩展名 （示例：当C2=宣传图 → /2023/Q3/宣传图.jpg）

   **错误处理机制**：

   ❗ 当检测到以下情况时：

   - 路径包含非法字符（如 \ : * ? " < > |）
   - 超过系统路径长度限制
   - 其他非法路径格式 

   📁 系统会自动保存到专用错误目录： ERROR-XXXXXXXX（示例：ERROR-4ab20ee1）

   示例输出结果：
   
   ```
   modified
   ├── ERROR-4ab20ee1
   │   ├── eee.jpg
   │   └── fff.jpg
   ├── SPU-1111
   │   ├── 一室一厅
   │   │   └── 商品展示图
   │   │       └── 701.jpg
   │   ├── 三室一厅
   │   │   └── 商品展示图
   │   │       └── 701.jpg
   │   └── 两室一厅
   │       └── 商品展示图
   │           └── 701.jpg
   ├── 一级目录
   │   └── 二级目录
   │       └── 三级目录
   │           └── 四级目录
   │               └── 五级目录
   │                   └── 六级目录
   │                       └── 七级目录
   │                           └── ddd.jpg
   └── 统计报表.xlsx
   ```

##### 处理结果解析
* **匹配成功：** 数据表与图片文件存在对应关系

* **表匹配失败：** 数据表中存在但无对应图片文件

* **图匹配失败：** 图片文件存在但无对应数据表记录

**统计报表示例**

![星开祈灵百宝箱 项目文档-30.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-30.png?v=1)

##### 水印参数配置
![星开祈灵百宝箱 项目文档-24.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-24.png?v=1)

![星开祈灵百宝箱 项目文档-25.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-25.png?v=1)

参数配置系统包含以下特性：
1. **预设模板模式**：
   - 参数锁定不可编辑
   - 支持模板即时切换应用
2. **自定义模式**：
   - 实时预览：参数调整后立即显示效果
   - 背景替换：支持上传自定义背景样式图
   - 临时保存：编辑期间参数自动缓存
   - 模板保存：点击【添加样式】永久存储配置
3. **更新机制**：

   - 非编辑状态下修改参数需点击【更新样式】生效
   - 支持通过悬浮按钮删除自定义模板及预览背景

4. **自定义字体设置：**
   * 如果系统中不存在指定字体，可以使用用户上传的字体文件

![星开祈灵百宝箱 项目文档-26.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-26.png?v=1)

![星开祈灵百宝箱 项目文档-27.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-27.png?v=1)

![星开祈灵百宝箱 项目文档-28.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-28.png?v=1)

##### 标准操作流程
1. **文件准备阶段**

   按规范制作包含图片文件与数据表的ZIP压缩包

2. **样式配置阶段**

   选择预设模板或创建自定义水印样式

3. **任务执行阶段**

   - 上传压缩文件后系统自动启动处理
   - 通过进度条监控任务状态
   - 完成100%后自动触发结果文件下载



## 🛠️ 技术栈

### 后端

- **框架** ：Spring Boot、Spring MVC、Spring Security、MyBatis
- **数据库** ：MySQL、Redis
- **第三方库** ：Lombok、Java Mail Sender、Fastjson、Apache Commons Compress、Apache POI
- **安全** ：BCryptPasswordEncoder、Remember-Me
- **工具与功能** ：线程管理、图像处理、文件上传、全局异常处理

### 前端

- **框架** ：Vue 3
- **状态管理** ：Pinia
- **UI 组件库** ：Element Plus、Element Plus Icons
- **路由** ：Vue Router
- **HTTP 请求库** ：Axios
- **构建工具** ：Vite
- **功能与工具**：
  - **图片懒加载** ：Intersection Observer API（自定义指令 `v-lazy`）
  - **动画** ：Element Plus 过渡动画（如 `<transition>` 组件）
  - **依赖注入** ：Vue `inject`/`provide`（动态标题传递）
  - **复杂表单验证** ：Element Plus 表单规则与自定义校验逻辑



## ✨技术栈亮点

### 后端

#### 1. 安全架构设计（Spring Security + 自定义拦截器）

**亮点描述：**

- **细粒度权限控制**：通过 `SecurityConfiguration` 配置 Spring Security，实现 `/api/auth/**` 免认证、其他接口需认证访问，结合 `@EnableWebSecurity` 实现安全过滤链
- **多态登录与退出**：自定义登录接口 `/api/auth/login` 和退出接口 `/api/auth/logout`，通过 `successHandler` 和 `failureHandler` 返回友好 JSON 响应（如 `RestBean.success("登录成功")`）
- **记住我功能**：使用 `JdbcTokenRepositoryImpl` 持久化 Remember-Me 令牌，支持 15 天有效期
- **CORS 跨域优化**：通过 `CorsConfigurationSource` 允许所有来源请求，支持凭证传递（`allowCredentials: true`），避免前后端联调问题

**代码片段：**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(e -> e // 免认证路径与权限控制
                .requestMatchers("/api/auth/**").permitAll() // 登录/注册接口免认证
                .anyRequest().authenticated() // 其他接口需认证
            )
            .formLogin(e -> e // 自定义登录退出逻辑
                .loginProcessingUrl("/api/auth/login") // 自定义登录URL
                .successHandler(this::onAuthenticationSuccess) // JSON格式响应
                .failureHandler(this::onAuthenticationFailure)
            )
            .logout(e -> e
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(this::onAuthenticationSuccess)
            )
            .rememberMe(e -> e // 记住我功能（15天有效期）
                .tokenValiditySeconds(3600 * 24 * 15) // 令牌有效期
                .tokenRepository(tokenRepository()) // 使用JDBC持久化令牌
            )
            .cors(e -> e.configurationSource(corsConfigurationSource())) // CORS全局配置
            .exceptionHandling(e -> e.authenticationEntryPoint(this::onAuthenticationFailure)) // 统一异常处理
            .build();
    }
}
```

#### 2. 复杂业务场景的异步处理（多线程 + 任务监控）

**亮点描述：**

- **线程池替代原生线程**：在 `BatchImageWatermarkerServiceImpl` 中使用 `ThreadPoolExecutor` 管理任务线程，支持动态调整线程数（核心线程数 2，最大 6）和任务队列（容量 10），避免资源耗尽风险
- **任务进度与结果管理**：通过 `ConcurrentHashMap` 缓存任务进度（`P`）、生成文件（`ZIP`）和附加信息（`INFO`），提供 `/get-progress` 和 `/get-zip-file` 接口实时查询
- **Future 实现任务可控性**：通过 `Future<?>` 对象和 `ConcurrentHashMap` 缓存任务状态，`stopTask` 方法调用 `future.cancel(true)` 实现更精准的任务终止，且通过统一的 `cleanupResources` 清理资源，防止内存泄漏
- **临时文件生命周期管理**：新增 `cleanupTempFiles` 定时任务（每小时执行），自动清理超过 1 小时的临时 ZIP 文件，避免磁盘空间浪费

**代码片段：**

```java
@Slf4j
@Service
public class BatchImageWatermarkerServiceImpl implements BatchImageWatermarkerService {

    private static final Map<String, Future<?>> T = new ConcurrentHashMap<>(); // 线程缓存
    private static final Map<String, Integer> P = new ConcurrentHashMap<>(); // 进度缓存
    private static final Map<String, File> ZIP = new ConcurrentHashMap<>(); // 文件缓存
    private static final Map<String, Map<String, Set<String>>> INFO = new ConcurrentHashMap<>(); // 信息缓存

    private final ExecutorService executor = new ThreadPoolExecutor( // 线程池
            2,
            6,
            30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    public void startTask(String username, InputStream fileStream, WatermarkParams params, Image backgroundImage) {
        cleanupResources(username);
        Future<?> future = executor.submit(() -> {
            try {
                handleFileUpload(username, fileStream, params, backgroundImage); // 处理图片并生成水印
            } catch (Exception e) {
                cleanupResources(username);
                log.error("进程崩溃：{}", e.getMessage());
                throw new RuntimeException(e);
            }
        });
        T.put(username, future);
    }

    private void cleanupResources(String username) { // 清理任务资源
        P.remove(username);
        T.remove(username);
        INFO.remove(username);
        ZIP.remove(username);
    }

    @Override
    public Integer getProgress(String username) { // 获取进度
        return P.getOrDefault(username, 0);
    }

    @Override
    public ResponseEntity<InputStreamResource> getZipFile(String username) { // 获取文件结果
        File file = ZIP.getOrDefault(username, null);
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modified.zip")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(new FileInputStream(file)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new InputStreamResource(new ByteArrayInputStream("{\"message\":\"服务器内部错误\"}".getBytes())));
        }
    }

    @Override
    public Map<String, Set<String>> getAdditionalData(String username) { // 获取附加信息
        return INFO.getOrDefault(username, null);
    }

    @Override
    public void stopTask(String username) { // 终止任务
        Future<?> future = T.get(username);
        if (future != null) future.cancel(true); // 安全终止线程
        cleanupResources(username);
    }

    @Scheduled(fixedRate = 3600 * 1000)
    public void cleanupTempFiles() { // 定时清理过期临时文件
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        File tempDir = new File(tmpDirPath);
        File[] files = tempDir.listFiles((dir, name) ->
                name.toLowerCase().startsWith("output") && name.toLowerCase().endsWith(".zip")
        );
        if (files != null) {
            long now = System.currentTimeMillis();
            for (File file : files) {
                try {
                    if (now - file.lastModified() > 3600 * 1000) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            log.info("已清理过期临时文件: {}", file.getAbsolutePath());
                        } else {
                            log.error("文件清理失败: {}", file.getAbsolutePath());
                        }
                    }
                } catch (SecurityException e) {
                    log.error("权限不足，无法删除文件: {}", file.getAbsolutePath());
                }
            }
        }
    }
}
```

#### 3. 数据层的高效设计（MyBatis + 分层映射）

**亮点描述：**

- **MyBatis 与 Spring Boot 的无缝集成**：通过 `@Mapper` 注解实现简洁的数据库操作（如 `UserMapper` 的 `createAccount` 方法）
- **DTO-Entity 转换**：在 `WatermarkParamsConvert` 中定义 `toDTO` 和 `toEntity` 方法，解耦传输对象（DTO）与实体（Entity），提升代码可维护性
- **复杂业务参数的序列化**：将 `WatermarkParams` 对象序列化为 JSON 存储（如 `WatermarkData` 的 `json` 字段），简化数据库设计

**代码片段：**

```java
@Mapper
public interface UserMapper {
    @Insert("insert into account (email, username, password) values (#{email}, #{username}, #{password})")
    int createAccount(String username, String password, String email); // 创建用户
}
```

```java
public class WatermarkParamsConvert { // 实现传输对象与实体的相互转换
    
    public static WatermarkDataDTO toDTO(WatermarkData w) {
        return new WatermarkDataDTO(
                w.getId(), w.getUsername(), toBase64(w.getBackgroundImage()), toBase64(w.getPreviewImage()),
                JSON.parseObject(w.getJson(), new TypeReference<WatermarkParamsDTO>() {})
        );
    }
    
    public static WatermarkData toEntity(WatermarkDataDTO w) {
        return new WatermarkData(
                w.getId(), w.getUsername(), toBytes(w.getBackgroundImage()), toBytes(w.getPreviewImage()),
                JSON.toJSONString(w.getParams())
        );
    }
}
```

#### 4. 异常处理与容错机制

**亮点描述：**

- **全局异常捕获**：通过 `@ControllerAdvice` 统一处理 `IllegalArgumentException` 和自定义异常（如 `FontNotFoundException`），返回结构化错误信息（如 `{"type": "FontNotFoundException", "message": "字体未安装"}`）
- **参数校验**：在 `AuthorizeController` 中使用 `@Pattern`、`@Length` 等注解进行参数校验，结合 `@Validated` 实现输入合法性检查

**代码片段：**

```java
@ControllerAdvice
public class GlobalExceptionHandler { // 全局异常处理器

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(IllegalArgumentException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("type", "IllegalArgumentException");
        errorResponse.put("message", e.getMessage());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
```

```java
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController { // 用户认证控制器

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";

    @PostMapping("/register")
    public RestBean<String> registerUser(
        @Pattern(regexp = USERNAME_REGEX) @Length(min = 2, max = 8) @RequestParam("username") String username,
        @Length(min = 6, max = 16) @RequestParam("password") String password,
        @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
        @Length(min = 6, max = 6) @RequestParam("code") String code,
        HttpSession session) {
        String s = service.validateAndRegister(username, password, email, code, session.getId());
        if (s == null) return RestBean.success("注册成功");
        else return RestBean.failure(400, s);
    }
}
```

#### 5. 图像处理与性能优化

**亮点描述：**

- **图片水印算法**：在 `ImageUtil` 中实现复杂水印逻辑（如背景图层、渐变色、描边、阴影、仿射变换），支持自定义字体（通过 `Font.createFont` 动态加载 TTF 文件）
- **零中间文件的 ZIP 流式生成**：基于 `ZipArchiveOutputStream` 实现内存到 ZIP 的直写流水线，图像处理后直接通过流式 API 写入压缩包，避免磁盘临时文件周转，降低 IO 开销
- **线程安全的临时文件管理**：通过 `FileUtil.deleteDirectory` 递归删除临时目录，确保资源及时释放

**代码片段：**

```java
public class ImageUtil {
public static BufferedImage handleWatermark(WatermarkParams p, Image image, Image backgroundImage, String watermark) throws IOException, FontNotFoundException { // 处理图片水印
        int width = image.getWidth(null), height = image.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(image, 0, 0, width, height, null);
        if (backgroundImage != null) { // 背景图层
            BufferedImage styleImage = toBufferedImage(backgroundImage);
            int styleWidth = styleImage.getWidth(), styleHeight = styleImage.getHeight();
            int minWidth = Math.min(width, styleWidth), minHeight = Math.min(height, styleHeight);
            int[] stylePixels = ((DataBufferInt) styleImage.getRaster().getDataBuffer()).getData();
            int[] destPixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
            for (int y = 0; y < minHeight; y++) {
                int styleRowOffset = y * styleWidth, destRowOffset = y * width;
                for (int x = 0; x < minWidth; x++) {
                    int stylePixel = stylePixels[styleRowOffset + x];
                    int styleAlpha = (stylePixel >>> 24) & 0xFF;
                    if (styleAlpha != 0) {
                        int destPixel = destPixels[destRowOffset + x];
                        int blendedPixel = getBlendedPixel(destPixel, stylePixel, styleAlpha);
                        destPixels[destRowOffset + x] = blendedPixel;
                    }
                }
            }
        }
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font font = getFont(p.getFontName(), p.getFontStyles(), p.getFontSize());
        if (font == null) throw new FontNotFoundException("字体 [" + p.getFontName() + "] 未安装在系统中");
        g.setFont(font); // 字体
        FontMetrics fm = g.getFontMetrics();
        if (p.getRotation() != 0 || p.getShearX() != 0) { // 仿射变换
            g.transform(getAffineTransform(p));
        }
        if (p.getShadowOpacity() != 0) { // 投影
            Composite originalComposite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.getShadowOpacity()));
            g.setColor(p.getShadowColor());
            int x = p.getX() + p.getShadowDx(), y = p.getY() + p.getShadowDy();
            for (int i = 0; i < watermark.length(); i++) {
                char c = watermark.charAt(i);
                g.drawString(String.valueOf(c), x, y);
                x += (p.getIncludeCharWidthInDx() ? fm.charWidth(c) : 0) + p.getDx();
                y += (p.getIncludeCharHeightInDy() ? fm.getAscent() : 0) + p.getDy();
            }
            g.setComposite(originalComposite);
        }
        if (p.getStrokeSize() != 0) { // 描边
            if (p.getStrokeGradient() != null) {
                WatermarkParams.GradientParams gp = p.getStrokeGradient();
                g.setPaint(new GradientPaint(gp.getStart(), gp.getStartColor(), gp.getEnd(), gp.getEndColor(), gp.getCyclic()));
            } else {
                g.setColor(p.getStrokeColor());
            }
            for (int i = 0, x = p.getX(), y = p.getY(), gap = p.getStrokeSize(); i < watermark.length(); i++) {
                char c = watermark.charAt(i);
                for (int j = -gap; j <= gap; j++) {
                    for (int k = -gap; k <= gap; k++) {
                        g.drawString(String.valueOf(c), x + j, y + k);
                    }
                }
                x += (p.getIncludeCharWidthInDx() ? fm.charWidth(c) : 0) + p.getDx();
                y += (p.getIncludeCharHeightInDy() ? fm.getAscent() : 0) + p.getDy();
            }
        }
        if (p.getFontGradient() != null) { // 渐变
            WatermarkParams.GradientParams gp = p.getFontGradient();
            g.setPaint(new GradientPaint(gp.getStart(), gp.getStartColor(), gp.getEnd(), gp.getEndColor(), gp.getCyclic()));
        } else {
            g.setColor(p.getFontColor());
        }
        for (int i = 0, x = p.getX(), y = p.getY(); i < watermark.length(); i++) {
            char c = watermark.charAt(i);
            g.drawString(String.valueOf(c), x, y);
            x += (p.getIncludeCharWidthInDx() ? fm.charWidth(c) : 0) + p.getDx();
            y += (p.getIncludeCharHeightInDy() ? fm.getAscent() : 0) + p.getDy();
        }
        g.dispose();
        return bufferedImage;
    }
}
```

```java
public void handleFileUpload(String username, InputStream fileStream, WatermarkParams params, Image backgroundImage) throws IOException, InterruptedException, FontNotFoundException {
    File input = Files.createTempDirectory("input").toFile();
    File output = Files.createTempFile("output", ".zip").toFile(); // 创建临时目录
    try {
        FileUtil.unzip(fileStream, input); // 解压 ZIP 文件到临时目录
        try (FileOutputStream fos = new FileOutputStream(output);
             ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(fos)) {
                        // ...
                        ImageUtil.handleImage(params, ImageIO.read(f), price, backgroundImage, zipOut);
                        zipOut.closeArchiveEntry();
                        tableNoMatch.remove(mid);
                        successMatch.add(mid);
                        // ...
                    }
                    int progress = ++validFilesCount * 100 / (files.length - 1);
                    if (progress == 100) progress = 99; // 等待收尾工作
                    P.put(username, progress); // 实时更新进度
                }
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            } finally {
                Map<String, Set<String>> info = new HashMap<>( // 收集任务执行的附加信息
                        Map.ofEntries(
                            Map.entry("successMatch", filterEmptyStrings(successMatch)),
                            Map.entry("tableNoMatch", filterEmptyStrings(tableNoMatch)),
                            Map.entry("imageNoMatch", filterEmptyStrings(imageNoMatch))
                ));
                INFO.put(username, info);
            }
        }
        ZIP.put(username, output); // 将最终的 ZIP 数据存入 Map
        P.put(username, 100); // 标记任务完成
    } finally {
        FileUtil.deleteDirectory(input); // 无论是否异常，删除临时目录释放资源
    }
}
```

#### 6. 可扩展的设计模式与工具类

**亮点描述：**

- **Builder 模式**：在 `WatermarkParams` 和 `WatermarkParamsDTO` 中使用 `@Builder` 注解，简化复杂对象的构造
- **参数化配置**：通过 `PresetStyleParams` 提供预设水印参数模板（如 `ImageUtil.P` 中的 5 种预设样式），降低客户端配置复杂度
- **工具类复用**：`FileUtil` 封装 ZIP 解压和目录删除，`CrawlerUtil` 实现网页爬虫的核心逻辑，提升代码复用率

**代码片段：**

```java
@With
@Builder
@Getter
public class WatermarkParams {

    public static final int N = 1200;

    // 位置（水印左下角的起始坐标）
    @Builder.Default private final Integer x = 0;
    @Builder.Default private final Integer y = N;

    // 字符偏移
    @Builder.Default private final Integer dx = 0;
    @Builder.Default private final Integer dy = 0;
    @Builder.Default private final Boolean includeCharWidthInDx = true;
    @Builder.Default private final Boolean includeCharHeightInDy = false;

    // 字体
    @Builder.Default private final String fontName = "微软雅黑";
    @Builder.Default private final Integer fontStyles = Font.PLAIN;
    @Builder.Default private final Integer fontSize = 100;
    @Builder.Default private final Color fontColor = Color.BLACK;

    // 描边
    @Builder.Default private final Integer strokeSize = 0;
    @Builder.Default private final Color strokeColor = Color.WHITE;

    // 投影
    @Builder.Default private final Integer shadowDx = 0;
    @Builder.Default private final Integer shadowDy = 0;
    @Builder.Default private final Color shadowColor = new Color(0, 0, 0, 0);
    @Builder.Default private final Float shadowOpacity = 0f;

    // 渐变
    @Builder.Default private final GradientParams fontGradient = null;
    @Builder.Default private final GradientParams strokeGradient = null;

    // 仿射变换
    @Builder.Default private final Float rotation = 0f;
    @Builder.Default private final Float shearX = 0f;
    @Builder.Default private final Boolean priorityRotation = true;

    @With
    @Builder
    @Getter
    public static class GradientParams {
        @Builder.Default private final Point start = new Point(0, 0);
        @Builder.Default private final Color startColor = Color.BLUE;
        @Builder.Default private final Point end = new Point(N, N);
        @Builder.Default private final Color endColor = Color.RED;
        @Builder.Default private final Boolean cyclic = true;
    }
}
```

```java
public class ImageUtil {

    public static final java.util.List<WatermarkParams> P = List.of(
            WatermarkParams.builder()
                    .x(90).y(1165).dx(-10)
                    .fontName("Source Han Sans CN").fontSize(130).fontColor(new Color(254, 249, 233))
                    .build(),
            WatermarkParams.builder()
                    .x(90).y(1165).dx(-10)
                    .fontName("Source Han Sans CN").fontSize(130)
                    .fontGradient(WatermarkParams.GradientParams.builder()
                            .startColor(new Color(254, 249, 233))
                            .end(new Point(80, 80))
                            .endColor(new Color(220, 176, 119))
                            .build())
                    .build(),
            WatermarkParams.builder()
                    .x(333).y(510).dy(-3)
                    .fontStyles(Font.BOLD | Font.ITALIC).fontSize(55).fontColor(Color.WHITE)
                    .strokeSize(5).strokeColor(new Color(250, 65, 10))
                    .build(),
            WatermarkParams.builder()
                    .x(70).y(705).dx(-8)
                    .fontName("DIN").fontStyles(Font.BOLD).fontColor(new Color(227, 34, 17))
                    .build(),
            WatermarkParams.builder()
                    .x(840).y(1175).dx(-3)
                    .fontName("HarmonyOS Sans").fontStyles(Font.BOLD | Font.ITALIC)
       				.fontSize(140).fontColor(new Color(0xfdf04a))
                    .strokeSize(6).strokeColor(new Color(0x065223))
                    .build()
    );
}
```

```java
public class FileUtil {

    public static final Set<String> TABLE = new HashSet<>(List.of( // 支持的表格形式
            "xls", "xlsx",
            "XLS", "XLSX"
    ));
    public static final Set<String> IMAGE = new HashSet<>(List.of( // 支持的图片形式
            "jpg", "jpeg", "png", "webp",
            "JPG", "JPEG", "PNG", "WEBG"
    ));
    public static final Pattern ERROR_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");
    public static final String ERROR_PATH = "ERROR-" + UUID.randomUUID().toString().substring(0, 8);

    public static void unzip(InputStream fileStream, File destDir) throws IOException { // 解压 ZIP 文件到目标目录
        try (ZipArchiveInputStream zipIn = new ZipArchiveInputStream(fileStream)) {
            ZipArchiveEntry entry;
            while ((entry = zipIn.getNextZipEntry()) != null) {
                File entryDestination = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    Files.createDirectories(entryDestination.getParentFile().toPath());
                    try (FileOutputStream fos = new FileOutputStream(entryDestination)) {
                        IOUtils.copy(zipIn, fos);
                    }
                }
            }
        }
    }

    public static void deleteDirectory(File dir) { // 递归删除目录
        if (dir.exists()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isDirectory()) deleteDirectory(file);
                file.delete();
            }
            dir.delete();
        }
    }

    public static boolean checkFileName(String name) { // 判断文件名合法性
        return ERROR_PATTERN.matcher(name).find();
    }
}
```

### 前端
#### 1. 模块化与组件化设计

- 使用 Vue3 实现逻辑复用，如`ForgetPage.vue`、`RegisterPage.vue`中复用表单验证逻辑
- 封装通用组件（如`Title.vue`），通过`inject`依赖注入实现动态标题配置，提升组件复用性
- 自定义指令`LazyLoad.js`实现图片懒加载，优化性能

#### 2. 响应式表单与复杂交互

- 多步骤表单动态切换（如`ForgetPage.vue`的分步密码重置），结合 Element Plus 的`ElSteps`和`transition`过渡动画
- 表单验证规则深度定制（如用户名仅允许中文/英文、密码一致性校验），支持异步验证（如邮件验证码请求）
- 实时参数配置面板（`BatchImageWatermarker.vue`），通过`watch`深度监听参数变化并动态生成预览图

#### 3. 路由与权限控制

- 路由守卫（`router.beforeEach`）实现全局权限控制，未登录用户强制跳转至登录页，已登录用户禁止访问欢迎页
- 动态路由配置（嵌套路由、子路由懒加载），优化首屏加载速度
- 路由元信息（`meta`）动态修改页面标题和图标

#### 4. 状态管理与持久化

- 使用 Pinia 管理全局状态（如用户登录信息），并通过`setupPersistedStore`插件实现状态持久化，避免刷新丢失数据
- 模块化 Store 设计（`stores/index.js`），分离认证状态与其他业务逻辑

#### 5. 网络请求封装与错误处理

- 统一封装网络请求（`net/index.js`），支持`_GET`、`_POST`等方法的错误拦截与消息提示
- 异步请求与 UI 联动（如`coldTime`倒计时控制验证码按钮禁用状态）

#### 6. 复杂文件处理与可视化

- 批量上传 ZIP 文件并解析（`BatchImageWatermarker.vue`），分块展示处理结果（成功匹配、表/图匹配失败）
- 图片预览功能支持自定义背景上传，结合后端接口生成水印效果图
- 进度条（`ElProgress`）实时显示处理进度

#### 7. UI/UX 细节优化

- 使用 Element Plus 过渡动画（如`ElFadeInLinear`）提升页面切换流畅度
- 统一的消息提示（`ElMessage`）与弹窗交互（`ElMessageBox`）
- 移动端适配（如`margin`、`flex`布局）和表单输入限制（如`maxlength`）



## 📊 代码量统计

数据截止至 **v1.5.4**

（单位：行，不包含文档代码）

|      项目名      | 后端 | 前端 | 总和 |
| :--------------: | :--: | :--: | :--: |
|    猜数字游戏    | 153  | 337  | 490  |
|    角色自截图    | 163  | 446  | 609  |
|    连连看游戏    | 169  | 1188 | 1357 |
|   简单爬虫脚本   | 250  | 217  | 467  |
| 批量图片水印处理 | 1179 | 1326 | 2505 |
|   （其他代码）   | 848  | 1388 | 2233 |
|     **总和**     | 2762 | 4899 | 7661 |



## 📂 项目结构

### 后端

本地服务拓扑图：

![星开祈灵百宝箱 项目文档-31.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-31.png?v=1)

```
backend
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── config
│   │   │           │   ├── SecurityConfiguration.java
│   │   │           │   └── WebConfiguration.java
│   │   │           ├── controller
│   │   │           │   ├── AuthorizeController.java
│   │   │           │   ├── BatchImageWatermarkerController.java
│   │   │           │   ├── CharacterMapController.java
│   │   │           │   ├── CrawlerController.java
│   │   │           │   ├── GuessNumberController.java
│   │   │           │   ├── IndexController.java
│   │   │           │   ├── LinkGameController.java
│   │   │           │   └── UserController.java
│   │   │           ├── convert
│   │   │           │   └── WatermarkParamsConvert.java
│   │   │           ├── dto
│   │   │           │   ├── ImageRequestDTO.java
│   │   │           │   ├── WatermarkDataDTO.java
│   │   │           │   └── WatermarkParamsDTO.java
│   │   │           ├── entity
│   │   │           │   ├── common
│   │   │           │   │   ├── Account.java
│   │   │           │   │   ├── AccountUser.java
│   │   │           │   │   └── RestBean.java
│   │   │           │   └── other
│   │   │           │       ├── GuessNumber.java
│   │   │           │       ├── ImageInfo.java
│   │   │           │       ├── WatermarkData.java
│   │   │           │       └── WatermarkParams.java
│   │   │           ├── exception
│   │   │           │   ├── FontNotFoundException.java
│   │   │           │   └── GlobalExceptionHandler.java
│   │   │           ├── interceptor
│   │   │           │   └── AuthorizeInterceptor.java
│   │   │           ├── mapper
│   │   │           │   ├── LinkMapper.java
│   │   │           │   ├── UserMapper.java
│   │   │           │   └── WatermarkMapper.java
│   │   │           ├── service
│   │   │           │   ├── api
│   │   │           │   │   ├── AuthorizeService.java
│   │   │           │   │   ├── BatchImageWatermarkerService.java
│   │   │           │   │   ├── CharacterMapService.java
│   │   │           │   │   ├── CrawlerService.java
│   │   │           │   │   ├── GuessNumberService.java
│   │   │           │   │   └── LinkGameService.java
│   │   │           │   └── impl
│   │   │           │       ├── AuthorizeServiceImpl.java
│   │   │           │       ├── BatchImageWatermarkerServiceImpl.java
│   │   │           │       ├── CharacterMapServiceImpl.java
│   │   │           │       ├── CrawlerServiceImpl.java
│   │   │           │       ├── GuessNumberServiceImpl.java
│   │   │           │       └── LinkGameServiceImpl.java
│   │   │           ├── util
│   │   │           │   ├── CrawlerUtil.java
│   │   │           │   ├── FileUtil.java
│   │   │           │   └── ImageUtil.java
│   │   │           └── BackendApplication.java
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── XkqlApplicationTest.java
├── static
│   ├── 图片
│   │   └── 默认背景图.jpg
│   ├── 表格
│   │   └── 默认统计报表.xlsx
│   └── 字体
│       ├── Source Han Sans CN.otf
│       ├── 黑体.ttf
│       ├── 楷体.ttf
│       └── 微软雅黑.ttf
└── pom.xml
```

### 前端

```
frontend
├── public
│   ├── icon
│   │   ├── BatchImageWatermarker.png
│   │   ├── CharacterMap.png
│   │   ├── Crawler.png
│   │   ├── Document.png
│   │   ├── GuessNumber.png
│   │   ├── LinkGame.png
│   │   └── Test.png
│   ├── 批量图片水印处理
│   │   ├── 纯白背景图.jpg
│   │   ├── 自定义样式 0.jpg
│   │   ├── 预设样式 1.jpg
│   │   ├── ...
│   │   └── 预设样式 5.jpg
│   ├── 角色图
│   │   ├── 保登心爱
│   │   │   ├── 保登心爱 001.jpg
│   │   │   ├── ...
│   │   │   └── 保登心爱 571.jpg
│   │   ├── 保登摩卡
│   │   │   ├── 保登摩卡 01.jpg
│   │   │   ├── ...
│   │   │   └── 保登摩卡 75.jpg
│   │   ├── 塞西莉亚
│   │   │   ├── 塞西莉亚 01.jpg
│   │   │   ├── ...
│   │   │   └── 塞西莉亚 48.jpg
│   │   ├── 天天座理世
│   │   │   ├── 天天座理世 001.jpg
│   │   │   ├── ...
│   │   │   └── 天天座理世 256.jpg
│   │   ├── 奈津惠
│   │   │   ├── 奈津惠 01.jpg
│   │   │   ├── ...
│   │   │   └── 奈津惠 74.jpg
│   │   ├── 宇佐美瑞希
│   │   │   ├── 宇佐美瑞希 01.jpg
│   │   │   ├── ...
│   │   │   └── 宇佐美瑞希 47.jpg
│   │   ├── 宇治松千夜
│   │   │   ├── 宇治松千夜 001.jpg
│   │   │   ├── ...
│   │   │   └── 宇治松千夜 195.jpg
│   │   ├── 提比
│   │   │   ├── 提比 01.jpg
│   │   │   ├── ...
│   │   │   └── 提比 46.jpg
│   │   ├── 条河麻耶
│   │   │   ├── 条河麻耶 01.jpg
│   │   │   ├── ...
│   │   │   └── 条河麻耶 96.jpg
│   │   ├── 桐间纱路
│   │   │   ├── 桐间纱路 001.jpg
│   │   │   ├── ...
│   │   │   └── 桐间纱路 275.jpg
│   │   ├── 豆馅
│   │   │   ├── 豆馅 1.jpg
│   │   │   ├── ...
│   │   │   └── 豆馅 5.jpg
│   │   ├── 野雁
│   │   │   ├── 野雁 1.jpg
│   │   │   ├── ...
│   │   │   └── 野雁 9.jpg
│   │   ├── 青山蓝山
│   │   │   ├── 青山蓝山 01.jpg
│   │   │   ├── ...
│   │   │   └── 青山蓝山 46.jpg
│   │   └── 香风智乃
│   │       ├── 香风智乃 001.jpg
│   │       ├── ...
│   │       └── 香风智乃 430.jpg
│   ├── 角色图压缩
│   │   ├── 保登心爱.zip
│   │   ├── 保登摩卡.zip
│   │   ├── 塞西莉亚.zip
│   │   ├── 天天座理世.zip
│   │   ├── 奈津惠.zip
│   │   ├── 宇佐美瑞希.zip
│   │   ├── 宇治松千夜.zip
│   │   ├── 提比.zip
│   │   ├── 条河麻耶.zip
│   │   ├── 桐间纱路.zip
│   │   ├── 豆馅.zip
│   │   ├── 野雁.zip
│   │   ├── 青山蓝山.zip
│   │   └── 香风智乃.zip
│   ├── favicon.png
│   ├── 产品使用手册.html
│   ├── 技术开发文档.html
│   └── 项目文档.html
├── src
│   ├── components
│   │   ├── layout
│   │   │   └── Title.vue
│   │   └── welcome
│   │       ├── ForgetPage.vue
│   │       ├── LoginPage.vue
│   │       └── RegisterPage.vue
│   ├── directives
│   │   └── lazyLoad.js
│   ├── net
│   │   └── index.js
│   ├── router
│   │   └── index.js
│   ├── stores
│   │   └── index.js
│   ├── views
│   │   ├── batchImageWatermarker
│   │   │   └── BatchImageWatermarker.vue
│   │   ├── characterMap
│   │   │   ├── CharacterMap.vue
│   │   │   └── CharacterMapSub.vue
│   │   ├── common
│   │   │   ├── Document.vue
│   │   │   ├── Index.vue
│   │   │   ├── Test.vue
│   │   │   └── Welcome.vue
│   │   ├── crawler
│   │   │   └── Crawler.vue
│   │   ├── guessNumber
│   │   │   ├── GuessNumber.vue
│   │   │   ├── HistoricalRecord.vue
│   │   │   └── Playing.vue
│   │   └── linkGame
│   │       ├── HistoricalRecord.vue
│   │       ├── Library.vue
│   │       ├── LinkGame.vue
│   │       └── Playing.vue
│   ├── App.vue
│   └── main.js
├── README.md
├── frontend.iml
├── index.html
├── jsconfig.json
├── package-lock.json
├── package.json
├── vite.config.js
└── vite.config.js.timestamp-1724388971674-1408e123a3c85.mjs
```



## 🔄 版本控制

**2024-8-22：[1.0.0]**

* 项目初始化
* 添加猜数字游戏

**2024-8-23：[1.1.0]**

* 添加角色自截图

**2024-8-28：[1.1.1]**

* 角色自截图添加批量下载按钮

**2024-9-8：[1.1.2]**

* 界面 UI 重构

**2024-9-26：[1.2.0]**

* 添加连连看游戏

**2024-10-6：[1.2.1]**

* 角色自截图资源更新

**2024-10-7：[1.3.0]**

* 添加声骸评分系统

**2024-10-19：[1.3.1]**

* 将声骸评分系统拆分为独立项目（因迁移操作未及时提交Git，此处为文档追溯补充）

  声骸评分系统项目的地址：https://github.com/KokoaChino/EchoScoringSystem

**2025-3-18：[1.3.2]**

* 版本号规范化（采用 SemVer 标准，主版本号重置）

**2025-4-1：[1.4.0]**

* 添加简单爬虫脚本

**2025-4-4：[1.5.0]**

* 添加批量图片水印处理

**2025-4-15：[1.5.1]**

* 批量图片水印处理添加自定义样式，样式持久化功能

**2025-4-26：[1.5.2]**

* 项目正式上线：http://8.138.214.176:5174
* 编写了完整的项目文档
* 邮件发送改为异步
* 新增支持上传 .otf 字体文件，完善了图片水印处理的字体选择逻辑

**2025-5-5：[1.5.3]**

* **架构优化**：
  * 重构前后端Base64传输协议，标准化数据字段结构（前后端仅传输原始二进制数据，前端渲染时动态添加data:image/jpeg;base64,头信息）
  * 实现图片水印处理路径自定义功能，支持输出目录指定与文件重命名规则配置
  * 完成MySQL和Redis数据库服务迁移，通过独立部署缓解主服务器资源压力

* **性能提升**：
    * 将任务处理线程模型升级为动态线程池，实现并发请求的弹性资源分配
    * 优化内存管理策略，采用流式处理替代全量二进制读写，配合定时任务自动清理缓存文件

* **功能增强**：
    * 新增PNG格式样式图层兼容支持，确保特效渲染结果图完整性
    * 增强用户认证体系与「记住我」功能稳定性，降低未授权访问(401)异常触发频率

* **移动端适配**：
    * 重构响应式布局框架，实现PC/移动端跨设备分辨率兼容

    * 优化触摸事件监听系统，使角色自截图、连连看游戏等交互组件全面支持移动端触控操作

* **文档完善**：
  * 主页新增产品使用手册与技术开发文档模块，提供终端用户操作指南与开发者技术说明

**2025-5-13：[1.5.4]**

* 批量图片水印处理新增功能：
  1. 复制样式操作
  2. 上传进度条
  3. 输出统计报表表格文件
* 优化了文档中心的交互



## 🤝 贡献指南

*🚧 文档完善中（预计下个主要版本更新）*

如有紧急贡献需求，请通过 [GitHub Issues](https://github.com/KokoaChino/xkql/issues) 或 📧 2178740980@qq.com 联系维护者



## 📜 许可证

本项目采用 **[MIT License](LICENSE)** 开源协议，核心条款如下：

✅ **允许的操作**：
- 自由使用、复制、修改、合并、发布、分发本项目
- 可用于商业用途
- 允许闭源二次开发

⚠️ **唯一义务**：
- **必须保留原始版权声明**（包含在项目所有副本中）

🛡️ **免责声明**：
> 本项目按"原样"提供，无任何明示或暗示的保证，包括但不限于适销性和特定用途适用性
>
> 作者/版权持有者对任何索赔、损害或其他责任不承担责任

完整法律文本请查看 [LICENSE](LICENSE) 文件



## ❓ 常见问题

### 在线访问
1. **文件处理过程中进度条卡住**

   ▸ 当前服务器配置为 2G 内存，上传超过 200MB 的 .zip 文件可能导致内存溢出 
   ▸ 临时方案：请将压缩包控制在 200MB 以内，分批上传处理 
   ▸ 优化计划：等我有钱了给服务器内存扩容

### 本地部署
1. **字体缺失报错处理**
   
   ```diff
   + 系统预设样式报错：
     1. 下载对应字体文件
     2. 安装至系统字体目录（Windows: C:\Windows\Fonts）
   
   + 自定义样式报错：
     请根据提示上传字体文件
   ```

2. **角色自截图批量下载不全** 
   
   ▸ *现象说明*：因 GitHub 仓库限制大文件上传，压缩包中仅包含阉割版角色图集
   
   ▸ *完整版获取方案*：  
   
     1. 打开本地项目目录 `xkql\frontend\public\角色图`  
     2. **手动全选所有文件夹** → 右键生成 .zip 压缩包  
     3. 将新压缩包**覆盖替换**到 `角色图压缩` 目录中即可  
   
    ▸ *注意事项*： 
    ⚠️ 完整图集约需 700M 存储空间，操作前请确保磁盘容量充足 
    ⚠️ 覆盖后需重启前端服务使变更生效
   
    ▸ *优化计划*： 
       长期计划迁移至 Git LFS 存储 (需企业版 GitHub)  

如有紧急问题反映，请通过 [GitHub Issues](https://github.com/KokoaChino/xkql/issues) 或 📧 2178740980@qq.com 联系维护者



## 📞 联系方式
- **QQ：**2178740980
- **微信：**ryu0785
- **GitHub Issues**：https://github.com/KokoaChino/xkql/issues



## 💌 反馈与支持

如遇系统异常或有优化建议，欢迎在 [GitHub Issues](https://github.com/KokoaChino/xkql/issues) 提交反馈。您的每一条意见都将推动项目迭代
若您认为此项目对您有所助益，您的 **Star** 或 **Pull Request** 将是我持续改进的最大动力

衷心感谢您的关注与支持！
—— 项目开发者 星开祈灵 (*´∀`)~♥ 

![星开祈灵百宝箱 项目文档-29.png](http://8.138.214.176:5174/项目图床/星开祈灵百宝箱%20项目文档-29.png?v=1)

