import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IScode } from '../scode.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../scode.test-samples';

import { ScodeService } from './scode.service';

const requireRestSample: IScode = {
  ...sampleWithRequiredData,
};

describe('Scode Service', () => {
  let service: ScodeService;
  let httpMock: HttpTestingController;
  let expectedResult: IScode | IScode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ScodeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Scode', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const scode = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Scode', () => {
      const scode = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Scode', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Scode', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Scode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addScodeToCollectionIfMissing', () => {
      it('should add a Scode to an empty array', () => {
        const scode: IScode = sampleWithRequiredData;
        expectedResult = service.addScodeToCollectionIfMissing([], scode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scode);
      });

      it('should not add a Scode to an array that contains it', () => {
        const scode: IScode = sampleWithRequiredData;
        const scodeCollection: IScode[] = [
          {
            ...scode,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScodeToCollectionIfMissing(scodeCollection, scode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Scode to an array that doesn't contain it", () => {
        const scode: IScode = sampleWithRequiredData;
        const scodeCollection: IScode[] = [sampleWithPartialData];
        expectedResult = service.addScodeToCollectionIfMissing(scodeCollection, scode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scode);
      });

      it('should add only unique Scode to an array', () => {
        const scodeArray: IScode[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scodeCollection: IScode[] = [sampleWithRequiredData];
        expectedResult = service.addScodeToCollectionIfMissing(scodeCollection, ...scodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scode: IScode = sampleWithRequiredData;
        const scode2: IScode = sampleWithPartialData;
        expectedResult = service.addScodeToCollectionIfMissing([], scode, scode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scode);
        expect(expectedResult).toContain(scode2);
      });

      it('should accept null and undefined values', () => {
        const scode: IScode = sampleWithRequiredData;
        expectedResult = service.addScodeToCollectionIfMissing([], null, scode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scode);
      });

      it('should return initial array if no Scode is added', () => {
        const scodeCollection: IScode[] = [sampleWithRequiredData];
        expectedResult = service.addScodeToCollectionIfMissing(scodeCollection, undefined, null);
        expect(expectedResult).toEqual(scodeCollection);
      });
    });

    describe('compareScode', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScode(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareScode(entity1, entity2);
        const compareResult2 = service.compareScode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareScode(entity1, entity2);
        const compareResult2 = service.compareScode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareScode(entity1, entity2);
        const compareResult2 = service.compareScode(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
