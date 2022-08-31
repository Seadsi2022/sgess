import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEvaleurvariable } from '../evaleurvariable.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../evaleurvariable.test-samples';

import { EvaleurvariableService } from './evaleurvariable.service';

const requireRestSample: IEvaleurvariable = {
  ...sampleWithRequiredData,
};

describe('Evaleurvariable Service', () => {
  let service: EvaleurvariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvaleurvariable | IEvaleurvariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EvaleurvariableService);
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

    it('should create a Evaleurvariable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const evaleurvariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evaleurvariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Evaleurvariable', () => {
      const evaleurvariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evaleurvariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Evaleurvariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Evaleurvariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Evaleurvariable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEvaleurvariableToCollectionIfMissing', () => {
      it('should add a Evaleurvariable to an empty array', () => {
        const evaleurvariable: IEvaleurvariable = sampleWithRequiredData;
        expectedResult = service.addEvaleurvariableToCollectionIfMissing([], evaleurvariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaleurvariable);
      });

      it('should not add a Evaleurvariable to an array that contains it', () => {
        const evaleurvariable: IEvaleurvariable = sampleWithRequiredData;
        const evaleurvariableCollection: IEvaleurvariable[] = [
          {
            ...evaleurvariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvaleurvariableToCollectionIfMissing(evaleurvariableCollection, evaleurvariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Evaleurvariable to an array that doesn't contain it", () => {
        const evaleurvariable: IEvaleurvariable = sampleWithRequiredData;
        const evaleurvariableCollection: IEvaleurvariable[] = [sampleWithPartialData];
        expectedResult = service.addEvaleurvariableToCollectionIfMissing(evaleurvariableCollection, evaleurvariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaleurvariable);
      });

      it('should add only unique Evaleurvariable to an array', () => {
        const evaleurvariableArray: IEvaleurvariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evaleurvariableCollection: IEvaleurvariable[] = [sampleWithRequiredData];
        expectedResult = service.addEvaleurvariableToCollectionIfMissing(evaleurvariableCollection, ...evaleurvariableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evaleurvariable: IEvaleurvariable = sampleWithRequiredData;
        const evaleurvariable2: IEvaleurvariable = sampleWithPartialData;
        expectedResult = service.addEvaleurvariableToCollectionIfMissing([], evaleurvariable, evaleurvariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaleurvariable);
        expect(expectedResult).toContain(evaleurvariable2);
      });

      it('should accept null and undefined values', () => {
        const evaleurvariable: IEvaleurvariable = sampleWithRequiredData;
        expectedResult = service.addEvaleurvariableToCollectionIfMissing([], null, evaleurvariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaleurvariable);
      });

      it('should return initial array if no Evaleurvariable is added', () => {
        const evaleurvariableCollection: IEvaleurvariable[] = [sampleWithRequiredData];
        expectedResult = service.addEvaleurvariableToCollectionIfMissing(evaleurvariableCollection, undefined, null);
        expect(expectedResult).toEqual(evaleurvariableCollection);
      });
    });

    describe('compareEvaleurvariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvaleurvariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEvaleurvariable(entity1, entity2);
        const compareResult2 = service.compareEvaleurvariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEvaleurvariable(entity1, entity2);
        const compareResult2 = service.compareEvaleurvariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEvaleurvariable(entity1, entity2);
        const compareResult2 = service.compareEvaleurvariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
