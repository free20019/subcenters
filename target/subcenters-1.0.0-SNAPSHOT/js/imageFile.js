(function (EXIF) {
	let defaultRotate = {
		'-7': -90,
		'-3': -90,
		0: 0,
		5: -90,
		'-5': 90,
		3: 90,
		7: 90,
		'-2': 180,
		2: 180,
	};
	let usuallyDirection = [1, 6, 3, 8];
	let mirrorDirection = [2, 5, 4, 7];
	let angle = [0, 90, 180, 270];
	if (!EXIF) console.error('没有引入EXIF.js插件');

	/**
	 * 判断是否图片格式
	 * @param type
	 * @returns {boolean}
	 */
	function imageType(type) {
		return !!type.indexOf('image/') >= 0;
	}

	/**
	 * 加载文件
	 * @param file
	 * @returns {Promise<any>}
	 */
	function loadFile(file) {
		return new Promise((resolve, reject) => {
			let reader = new FileReader();
			reader.onload = () => resolve(reader.result);
			reader.onerror = reject;
			reader.readAsDataURL(file);
		});
	}

	/**
	 * 加载图片
	 * @param src
	 * @returns {Promise<any>}
	 */
	function loadImageFile(src) {
		return new Promise((resolve, reject) => {
			let img = new Image();
			img.onload = () => resolve(img);
			img.onerror = reject;
			img.src = src
		});
	}

	/**
	 * 获取EXIF信息
	 * @param img
	 * @returns {Promise<any>}
	 */
	function loadEXIFFile(img) {
		return new Promise((resolve, reject) => {
			EXIF.getData(img, function () {
				let tags = EXIF.getAllTags(img);
				resolve({
					Orientation: tags.Orientation || 1,
					width: tags.PixelXDimension || img.width,
					height: tags.PixelYDimension || img.height,
					...tags
				})
			})
		})
	}

	/**
	 * 转成base64格式
	 * @param img
	 * @param exif
	 * @param options
	 * @returns {{data, originalPath: string | *, exif: *}}
	 */
	function getBase64(img, exif, options) {
		let data = {};
		let maxSize = options && options.maxSize || 200;
		let toRotate = options && options.toRotate || 1;
		let toMirror = options && options.toMirror || 1;
		let oF = !exif.newOrientation || exif.newOrientation === 1 && toRotate === 1 && toMirror === 1;
		let orientation = oF ? exif.Orientation : exif.newOrientation;
		let canvas = document.createElement('canvas');
		let ctx = canvas.getContext('2d');
		let {width, height} = img;
		/* 根据最长的方向进行缩小 */
		let {drawW, drawH} = imageZoom(width, height, maxSize);
		/* 判断orientation大于4宽高替换 */
		let {x, y} = imageLAVRotation(data, {
			context: ctx,
			canvas,
			drawWidth: drawW,
			drawHeight: drawH,
			rotate: orientation,
			toRotate
		});
		/* 判断镜像图片进行反转 */
		imageMirror(ctx, orientation, toMirror);
		/* 判断方向,默认回归到1 */
		data.orientation = exif.newOrientation = imageDirection(ctx, orientation, toRotate);
		/* 画图并返回 */
		ctx.drawImage(img, -x, -y, drawW, drawH);
		data.src = canvas.toDataURL();
		return {originalPath: data.src, data, exif}
	}

	/**
	 * 根据最长的方向进行缩小，并且scale最大值等于1
	 * @param width：宽
	 * @param height：高
	 * @param maxSize：最大值
	 * @returns {{drawH: number, drawW: number}}
	 */
	function imageZoom(width, height, maxSize) {
		let scale = width > height ? maxSize / width : maxSize / height;
		if (scale > 1) scale = 1;
		return {drawW: width * scale, drawH: height * scale}
	}

	/**
	 * 根据要旋转方向调整宽高
	 * @param data
	 * @param context
	 * @param canvas
	 * @param drawWidth
	 * @param drawHeight
	 * @param rotate
	 * @param toRotate
	 * @returns {{x, y}}
	 */
	function imageLAVRotation(data, {context, canvas, drawWidth, drawHeight, rotate, toRotate}) {
		// 设置宽高
		function setData(width, height) {
			data.width = width;
			data.height = height;
		}

		// 设置画布的宽高
		function setCanvas(width, height) {
			setData(width, height);
			canvas.width = width;
			canvas.height = height;
			/* 偏移到中心点 */
			let x = width / 2;
			let y = height / 2;
			context.translate(x, y);
			if (hasRotation(rotate, toRotate)) [x, y] = [y, x];
			return {x, y};
		}

		// 判断是否旋转
		function hasRotation(rotate, toRotate) {
			return !(rotate > 4 && toRotate > 4 || rotate <= 4 && toRotate <= 4)
		}

		let width = hasRotation(rotate, toRotate) ? drawHeight : drawWidth;
		let height = hasRotation(rotate, toRotate) ? drawWidth : drawHeight;
		return setCanvas(width, height);
	}

	/**
	 * 翻转镜像图片
	 * @param context
	 * @param orientation: 方位
	 */
	function imageMirror(context, orientation, toMirror = 1) {
		let rotate = usuallyDirection.indexOf(orientation) >= 0;
		let mirror = mirrorDirection.indexOf(orientation) >= 0;
		if (rotate && toMirror !== 1 || mirror && toMirror !== 2) context.scale(-1, 1);
		// switch (orientation) {
		// 	case 2:
		// 	case 4:
		// 	case 5:
		// 	case 7:
		// 		context.scale(-1, 1);
		// 		break;
		// }
	}

	/**
	 * 旋转方向
	 * @param context
	 * @param orientation
	 * @param toRotate
	 */
	function imageDirection(context, orientation, toRotate = 1) {
		let rotate = turnUsuallyDirection(orientation);
		context.rotate(defaultRotate[toRotate - rotate] * (Math.PI / 180));
		return toRotate;
	}

	/**
	 * 转成通常使用的四个方向
	 * @param orientation
	 * @returns {*}
	 */
	function turnUsuallyDirection(orientation) {
		if (orientation === 2 || orientation === 4) return orientation - 1;
		else if (orientation === 5 || orientation === 7) return orientation + 1;
		else return orientation;
	}

	/**
	 * 角度转方位
	 * @param rotate
	 * @param toRotate
	 * @returns {*}
	 */
	function rotationAngle(rotate, toRotate) {
		function forIndex(index, size) {
			if (index > size - 1) return 0;
			else if (index < 0) return size - 1;
			else return index;
		}

		let angleIndex = angle.indexOf(Math.abs(toRotate));
		let usuallyIndex = usuallyDirection.indexOf(rotate);
		let mirrorIndex = mirrorDirection.indexOf(rotate);
		if (angleIndex < 0) return rotate;
		angleIndex = toRotate < 0 ? -angleIndex : angleIndex;
		if (usuallyIndex >= 0) {
			let nUIndex = usuallyIndex + angleIndex;
			let index = forIndex(nUIndex, usuallyDirection.length);
			return usuallyDirection[index];
		} else if (mirrorIndex >= 0) {
			let nMIndex = mirrorIndex + angleIndex;
			return mirrorDirection[nMIndex > 3 ? 0 : nMIndex];
		}
	}

	function ImageFile() {}

	/**
	 * 批量加载图片
	 * @param imageArray
	 * @param files
	 * @returns {Promise<void>}
	 */
	ImageFile.loadImageFileAll = async function (imageArray, files) {
		for (let i = 0; i < files.length; i++) {
			let imageList = (function (images, file) {
				ImageFile.loadImageFile(file).then(item => {
					images.push(item);
				});
			})(imageArray, files[i]);
			setTimeout(imageList, 0);
		}
	};
	/**
	 * 加载图片
	 * @param file
	 * @returns {Promise<*>}
	 */
	ImageFile.loadImageFile = async function (file) {
		if (!imageType(file.type)) return null;
		try {
			let src = await loadFile(file);
			let img = await loadImageFile(src);
			let exif = await loadEXIFFile(img);
			return {name: file.name, ...getBase64(img, exif), originalPath: src};
		} catch (e) {
			throw e
		}
	};
	/**
	 * 获取图片
	 * @param src
	 * @param options
	 * @returns {Promise<*>}
	 */
	ImageFile.getImageFile = async function (src, options) {
		if (!imageType(src)) return null;
		try {
			let img = await loadImageFile(src);
			let exif = options.exif || await loadEXIFFile(img);
			return {...options, ...getBase64(img, exif, options.options), originalPath: src};
		} catch (e) {
			throw e
		}
	};
	/**
	 * 旋转图片
	 * @param src
	 * @param options
	 * @returns {Promise<*>}
	 */
	ImageFile.rotateImage = async function (src, options) {
		if (!imageType(src)) return null;
		let mirror = false;
		let rotate = options.options.rotate;
		let {toRotate, toMirror} = resetOptions({exif: options.exif, mirror, tRotate: rotate});
		try {
			let img = await loadImageFile(src);
			let exif = options.exif || await loadEXIFFile(img);
			return {
				originalPath: src,
				options: {...options.options}, ...getBase64(img, exif, {...options.options, toRotate, toMirror})
			};
		} catch (e) {
			throw e;
		}
	};

	function whetherToMirror(mirror, rotate){
		let currentDirection = usuallyDirection.indexOf(rotate) >= 0 ? 1 : 2;
		if (mirror) return currentDirection === 1 ? 2 : 1;
		else return currentDirection;
	}
	function resetOptions({exif, tRotate, mirror}) {
		let rotate = exif.newOrientation;
		let toRotate = rotationAngle(rotate, tRotate);
		let toMirror = whetherToMirror(mirror, rotate);
		return {rotate, toRotate, toMirror}
	}
	ImageFile.mirrorImage = async function(src, options) {
		if (!imageType(src)) return null;
		let mirror = options.options.mirror;
		let rotate = 0;
		let {toRotate, toMirror}=resetOptions({exif: options.exif, mirror, tRotate: rotate});
		try {
			let img = await loadImageFile(src);
			let exif = options.exif || await loadEXIFFile(img);
			return {
				originalPath: src,
				options: {...options.options}, ...getBase64(img, exif, {...options.options, toRotate, toMirror})
			};
		} catch (e) {
			throw e;
		}
	};
	window.ImageFile = ImageFile;
})({});