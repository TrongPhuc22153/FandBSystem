export default function UserContent(){
    return(
        <>
            <form className="editor">
                <div className="editor-toolbar">
                    <button className="editor-toolbar-item icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M5 9.5C8.5 9.5 11.5 9.5 15 9.5C15.1615 9.5 19 9.5 19 13.5C19 18 15.2976 18 15 18C12 18 10 18 7 18" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                            <path d="M8.5 13C7.13317 11.6332 6.36683 10.8668 5 9.5C6.36683 8.13317 7.13317 7.36683 8.5 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                        </svg>

                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M19 9.5C15.5 9.5 12.5 9.5 9 9.5C8.83847 9.5 5 9.5 5 13.5C5 18 8.70237 18 9 18C12 18 14 18 17 18" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                            <path d="M15.5 13C16.8668 11.6332 17.6332 10.8668 19 9.5C17.6332 8.13317 16.8668 7.36683 15.5 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                        </svg>
                    </button>
                    <span className="separator"></span>
                    <button className="editor-toolbar-item dropdown">
                        <span>Paragraph</span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <polyline points="208 96 128 176 48 96" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></polyline>
                        </svg>
                    </button>
                    <span className="separator"></span>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <path d="M64,120h88a40,40,0,0,1,0,80l-88.00586-.00488v-152L140,48a36,36,0,0,1,0,72" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="151.99414" y1="55.99512" x2="103.99414" y2="199.99512" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="63.99414" y1="199.99512" x2="143.99414" y2="199.99512" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="111.99414" y1="55.99512" x2="191.99414" y2="55.99512" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                        </svg>

                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="40" y1="128" x2="216" y2="128" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <g>
                                <path d="M76.334,96.00294A25.48209,25.48209,0,0,1,75.11111,88c0-22.09139,21.96094-40,52.88889-40,23.77865,0,42.25677,10.58606,49.529,25.52014" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                                <path d="M72,168c0,22.09139,25.07205,40,56,40s56-17.90861,56-40c0-23.76634-21.62275-32.97043-45.59723-40.00076" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                            </g>
                        </svg>

                    </button>
                    <span className="separator"></span>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <rect x="40" y="40" width="176" height="176" rx="8" strokeWidth="16" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" fill="none"></rect>
                            <path d="M215.99982,159.99982l-42.343-42.343a8,8,0,0,0-11.3137,0l-44.6863,44.6863a8,8,0,0,1-11.3137,0l-20.6863-20.6863a8,8,0,0,0-11.3137,0L40,176" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                            <circle cx="100" cy="92" r="12"></circle>
                        </svg>
                    </button>

                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="94.05511" y1="161.93204" x2="161.93736" y2="94.04979" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <path d="M144.96473,178.9102l-28.28427,28.28427a48,48,0,0,1-67.88225-67.88225L77.08248,111.028" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                            <path d="M178.91069,144.96424,207.195,116.68a48,48,0,0,0-67.88225-67.88225L111.02844,77.082" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <path d="M200,224.00005H55.99219a8,8,0,0,1-8-8V40a8,8,0,0,1,8-8L152,32l56,56v128A8,8,0,0,1,200,224.00005Z" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                            <polyline points="152 32 152 88 208.008 88" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></polyline>
                            <line x1="96" y1="136" x2="160" y2="136" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="96" y1="168" x2="160" y2="168" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <rect x="44" y="44" width="168" height="168" rx="8" strokeWidth="16" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" fill="none"></rect>
                            <line x1="128" y1="44" x2="128" y2="212" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="212" y1="128" x2="44" y2="128" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <path d="M24,60H152a32,32,0,0,1,32,32v96a8,8,0,0,1-8,8H48a32,32,0,0,1-32-32V68A8,8,0,0,1,24,60Z" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                            <polyline points="184 112 240 80 240 176 184 144" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></polyline>
                        </svg>
                    </button>
                    <span className="separator"></span>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <polyline points="64 88 16 128 64 168" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></polyline>
                            <polyline points="192 88 240 128 192 168" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></polyline>
                            <line x1="160" y1="40" x2="96" y2="216" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <path d="M108,144H40a8,8,0,0,1-8-8V72a8,8,0,0,1,8-8h60a8,8,0,0,1,8,8v88a40,40,0,0,1-40,40" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                            <path d="M224,144H156a8,8,0,0,1-8-8V72a8,8,0,0,1,8-8h60a8,8,0,0,1,8,8v88a40,40,0,0,1-40,40" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                        </svg>
                    </button>
                    <span className="separator"></span>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="88" y1="64" x2="216" y2="64" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="88.00614" y1="128" x2="216" y2="128" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="88.00614" y1="192" x2="216" y2="192" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <circle cx="44" cy="64" r="12"></circle>
                            <circle cx="44" cy="128" r="12"></circle>
                            <circle cx="44" cy="192" r="12"></circle>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="104" y1="128" x2="215.99902" y2="128" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="104" y1="64" x2="215.99902" y2="64" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="103.99902" y1="192" x2="215.99902" y2="192" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <polyline points="40 60 56 52 56 107.994" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></polyline>
                            <path d="M41.10018,152.55059A14.00226,14.00226,0,1,1,65.609,165.82752L40,200H68" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></path>
                        </svg>
                    </button>
                    <span className="separator"></span>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="40" y1="68" x2="216" y2="68" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="40" y1="108" x2="168" y2="108" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="40.00614" y1="148" x2="216" y2="148" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="40.00614" y1="188" x2="168" y2="188" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="40" y1="68" x2="216" y2="68" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="64" y1="108" x2="192" y2="108" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="40.00307" y1="148" x2="215.99693" y2="148" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="64.00307" y1="188" x2="191.99693" y2="188" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                        </svg>
                    </button>
                    <button className="editor-toolbar-item icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="192" height="192" fill="#000000" viewBox="0 0 256 256">
                            <rect width="256" height="256" fill="none"></rect>
                            <line x1="40" y1="68" x2="216" y2="68" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="88" y1="108" x2="216" y2="108" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="40.00614" y1="148" x2="216" y2="148" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                            <line x1="88.00614" y1="188" x2="216" y2="188" fill="none" stroke="#000000" strokeLinecap="round" strokeLinejoin="round" strokeWidth="16"></line>
                        </svg>
                    </button>
                </div>
                <div className="editor-textarea">
                    <div className="editor-textarea-editable" contentEditable spellCheck="false">
                        <h1>What web designers can learn from artists - from Van Gogh to Lloyd Wright</h1>
                        <p className="leading">Art in it's classic form, like painting and sculpting, is not
                            that different to the creations of web and UI designers. Even though their
                            purpose is different - as the goal of great web design is to enhance user
                            experiences - there's still a lot to learn from the former.</p>
                        <p>By now you may have been intruiged to read this article, but I'm sorry to inform
                            you that this is not some 'Medium-like-inspirational-design-take-kind-of-story'.
                            This is just an article full of gibberish, written in a way that makes it look
                            <img src="https://assets.codepen.io/285131/painting.jpg" />like a real article, but it's not. It's sole
                            purpose is to look good in this
                            CodePen demo, this CMS design concept. The image has also nothing to do with the
                            accompanied text, it's just a filler like everything else. You see, I feel it's
                            sometimes better to use 'real' text rather than 'Lorem Ipsum'. However, this
                            text is not <em>real</em>. Well, of course it's <strong>real</strong>, since
                            someone has written it (me), but it's not a real blog post.
                        </p>
                        <p>For designers it's important, or even necessary to, use real data instead of
                            'Lorem ipsum'. Sure, the latin excerpt is popular, but the problem is that you
                            don't understand what it says there. This is not just a problem for articles,
                            but for UI in general. How do you think the site structure on the left would
                            look like if I replaced 'Product' and 'Pricing' with 'Lorem Ipsum' and 'Dolor
                            Sit'? Doesn't give you the right impression, does it? And I know what you're
                            thinking: 'Product' and 'Pricing' isn't real data either, it's just made up
                            names of pages. That's true - but it's still better than the alternative. Also,
                            after all, this is just a pen, and this design is just a concept. But it looks
                            legit, right?</p>
                        <h2>Why 'Form Follows Function' is a valid rule to live by in web design</h2>
                        <p>Use text that your client provides. Give your UI good labels, labels that are actually going to be used in production. There's several reasons as to why it's better to use real data instead of 'Lorem Ipsum':
                        </p>

                        <ul>
                            <li>It's more readable (obviously)</li>
                            <li>You stress test your designs, meaning that you'll become more aware of errors caused by line-breaks and text overflow.</li>
                            <li>It's more fun and rewarding, since you've designed something that looks <em>real</em></li>
                        </ul>
                        <p>
                            Don't read to much into this, though. This article was never meant to convince you to stop using 'Lorem Ipsum', this is in fact just an article full of gibberish.</p>

                        <h2>More filler content</h2>
                        <p>I could've easily written about how you make potato salad without potatoes, or written a fairytale about potato salad princes and potato salad princesses, instead.
                        </p>

                        <p>I did not. At least I saved the potato salad part of this article to a section further down, so far down that you'll perhaps have to scroll to read it (depending on your screen size), hopefully saving you from thinking "Why does he mention potato salad in an article about web design?" before you've taken a look at the CMS design concept. Which again is the whole purpose of this pen.</p>
                        <p>If you liked this pen I suggest you should check out <a href="https://codepen.io/havardob" contentEditable="false" target="_blank">some of my other pens</a>.</p>
                    </div>
                </div>
            </form>
            <div className="window-main-body-right">
                <section className="settings-section">
                    <h2 className="section-title">Settings</h2>

                    <div className="input-group">
                        <label className="input-label">Slug</label>
                        <input type="text" className="input-field" value="what-web-designers-can-learn-from-artists-from-van-gogh-to-lloyd-wright" />
                    </div>
                    <div className="input-group">
                        <label className="input-label">Full URL</label>
                        <a href="#" className="input-url">https://bold.io/blog/what-web-designers-can-learn-from-artists-from-van-gogh-to-lloyd-wright</a>
                    </div>
                </section>
                <section className="settings-section">
                    <h2 className="section-title">Social</h2>
                    <div className="input-group">
                        <label className="input-label">Featured Image</label>
                        <button className="input-image">
                            <span className="input-image-wrapper">
                                <img src="https://assets.codepen.io/285131/painting.jpg" />
                            </span>
                            <span className="input-image-meta">
                                <span className="input-image-meta-title">fb-painting.jpg</span>
                                <span className="input-image-meta-action">Change</span>
                            </span>
                        </button>
                    </div>
                    <div className="input-group">
                        <label className="input-label">Meta Title</label>
                        <input type="text" className="input-field" value="What web designers can learn from artists - from Van Gogh to Lloyd Wright" />
                    </div>
                    <div className="input-group">
                        <label className="input-label">Meta Description</label>
                        <textarea className="input-field input-field--textarea">Art in it's classic form, like painting and sculpting, is not that different to the creations of web and UI designers. Even though their purpose is different - as the goal of great web design is to enhance user experiences - there's still a lot to learn from the former.</textarea>
                    </div>
                </section>
                <section className="settings-section">
                    <h2 className="section-title">Byline</h2>
                    <div className="input-group">
                        <label className="input-checkbox">
                            <input type="checkbox" className="input-checkbox-box" checked />
                            <span className="input-checkbox-toggle"></span>
                            <span className="input-checkbox-label">Show author</span>
                        </label>
                        <label className="input-checkbox">
                            <input type="checkbox" className="input-checkbox-box" checked />
                            <span className="input-checkbox-toggle"></span>
                            <span className="input-checkbox-label">Date published</span>
                        </label>
                        <label className="input-checkbox">
                            <input type="checkbox" className="input-checkbox-box" />
                            <span className="input-checkbox-toggle"></span>
                            <span className="input-checkbox-label">Date edited</span>
                        </label>
                    </div>
                </section>
                <section className="settings-section">
                    <h2 className="section-title">Actions</h2>
                    <button className="button button--delete">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M19 11V20.4C19 20.7314 18.7314 21 18.4 21H5.6C5.26863 21 5 20.7314 5 20.4V11" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                            <path d="M10 17V11" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                            <path d="M14 17V11" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                            <path d="M21 7L16 7M3 7L8 7M8 7V3.6C8 3.26863 8.26863 3 8.6 3L15.4 3C15.7314 3 16 3.26863 16 3.6V7M8 7L16 7" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                        </svg>
                        Move to trash
                    </button>
                </section>
            </div>
        </>
    )
}